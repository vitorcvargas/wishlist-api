package com.dev.wishlist.services;

import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.exceptions.NotFoundException;
import com.dev.wishlist.mappers.WishlistMapper;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import com.dev.wishlist.rest.dtos.WishlistDTO;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static com.dev.wishlist.utils.APIConstants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class WishlistService {

    private final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final ProductCatalogRepository productCatalogRepository;
    private final WishlistNotifierService notifier;

    public WishlistService(final WishlistRepository wishlistRepository, final ProductCatalogRepository productCatalogRepository, final WishlistNotifierService notifier) {
        this.wishlistRepository = wishlistRepository;
        this.productCatalogRepository = productCatalogRepository;
        this.notifier = notifier;
    }

    public WishlistResponse createWishlist(Long userId, WishlistDTO wishlistDTO) {
        logger.info("action=started_creating_wishlist, userId={}, wishlistDTO={}", userId, wishlistDTO);

        final var isWishlistAlreadyPresent = wishlistRepository.findByUserId(userId)
                .orElse(new ArrayList<>())
                .stream()
                .anyMatch(wishlist -> wishlist.getName().equals(wishlistDTO.getName()));

        if (isWishlistAlreadyPresent)
            throw BadRequestException.wishlistAlreadyCreatedWithName(wishlistDTO.getName(), userId);

        Wishlist wishlist = new Wishlist();
        wishlist.setName(wishlistDTO.getName());
        wishlist.setUserId(userId);
        wishlist.setPublic(wishlistDTO.getPublic());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        logger.info("action=finished_creating_wishlist, userId={}, wishlistDTO={}", userId, wishlistDTO);

        return new WishlistResponse(savedWishlist.getId(), savedWishlist.getName(), userId, new ArrayList<>(), savedWishlist.isPublic());
    }

    public List<WishlistResponse> getAllWishlists(Long userId) {
        logger.info("action=started_finding_all_wishlists, userId={}", userId);

        final var wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, null));
        final var response = new ArrayList<WishlistResponse>();

        wishlist.forEach(wish -> {
            List<Long> productIds = wish.getProducts().stream().map(Product::getProductId).toList();

            final var products =
                    StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                            .toList();

            response.add(WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(wish, products));
        });

        logger.info("action=finished_finding_all_wishlists, userId={}", userId);

        return response;
    }

    public WishlistResponse updateWishlist(Long userId, String wishlistId, WishlistDTO wishlistDTO) {
        logger.info("action=started_updating_wishlist, userId={}, wishlist={}, wishlistUpdate={}", userId, wishlistId, wishlistDTO);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, wishlistId));

        List<Long> productIds = wishlist.getProducts().stream().map(Product::getProductId).toList();

        final var products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        updateWishlistDBFromRequest(wishlist, wishlistDTO);

        wishlistRepository.save(wishlist);

        logger.info("action=finished_updating_wishlist, userId={}, wishlistId={}, wishlistUpdate={}", userId, wishlistId, wishlistDTO);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(wishlist, products);
    }

    public void deleteWishlist(final Long userId, final String wishlistId) {
        logger.info("action=started_remove_wishlist, userId={}, wishlistId={}", userId, wishlistId);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, wishlistId));

        wishlistRepository.delete(wishlist);

        logger.info("action=finished_remove_wishlist, userId={}, wishlistId={}", userId, wishlistId);
    }

    public void addToWishlist(final Product product, final Long userId, final String wishlistId) {
        logger.info("action=started_adding_product_to_wishlist product={}", product);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, wishlistId));

        final var wishlistSize = wishlist.getProducts().size();

        if (wishlistSize == MAX_WISHLIST_SIZE)
            throw BadRequestException.wishlistLimitReached();

        if (wishlist.getProducts().contains(product))
            throw BadRequestException.productAlreadyAddedToWishlist();

        wishlist.addProduct(product);

        wishlistRepository.save(wishlist);
        notifier.notify(PRODUCT_ADDED_TOPIC, userId, product.getProductId());

        logger.info("action=finished_adding_product_to_wishlist product={}", product);
    }

    public WishlistResponse filterProducts(final Long userId, final String wishlistId, final String searchInput) {
        logger.info("action=started_finding_products, userId={}, wishlistId={}, searchInput={}", userId, wishlistId, searchInput);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, wishlistId));

        final var productIds = wishlist.getProducts()
                .stream()
                .filter(product -> product.getName().toLowerCase().matches(format(".*%s.*", searchInput.toLowerCase())))
                .map(Product::getProductId)
                .toList();

        if (productIds.isEmpty())
            throw NotFoundException.productNotFoundWithSearchInput(searchInput, userId, wishlistId);

        final var products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        logger.info("action=finished_finding_products, userId={}, wishlistId={}, searchInput={}", userId, wishlistId, searchInput);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(wishlist, products);
    }

    public WishlistResponse findAllProducts(final Long userId, final String wishlistId) {
        logger.info("action=started_finding_all_products, userId={}, wishlistId={}", userId, wishlistId);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.productNotFound(userId, wishlistId));

        List<Long> productIds = wishlist.getProducts().stream().map(Product::getProductId).toList();

        final var products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        logger.info("action=finished_finding_all_products, userId={}, wishlistId={}", userId, wishlistId);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(wishlist, products);
    }

    public void deleteProduct(final Long userId, final String wishlistId, final Long productId) {
        logger.info("action=started_deleting_product, userId={}, wishlist={}, productId={}", userId, wishlistId, productId);

        final var wishlist = wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId)
                .orElseThrow(() -> NotFoundException.wishlistNotFound(userId, wishlistId));

        final var productOptional = wishlist.getProducts()
                .stream()
                .filter(product -> Objects.equals(product.getProductId(), productId))
                .findFirst();

        if (productOptional.isEmpty())
            throw NotFoundException.productNotFoundWithId(productId, userId, wishlistId);

        wishlist.getProducts().remove(productOptional.get());

        wishlistRepository.save(wishlist);
        notifier.notify(PRODUCT_DELETED_TOPIC, userId, productId);

        logger.info("action=finished_deleting_product, userId={}, wishlistId={}, productId={}", userId, wishlistId, productId);
    }

    private void updateWishlistDBFromRequest(final Wishlist wishlist, final WishlistDTO request) {
        final var name = request.getName();
        final var isPublic = request.getPublic();

        if (!isEmpty(name))
            wishlist.setName(name);
        if (!isNull(isPublic))
            wishlist.setPublic(isPublic);
    }
}
