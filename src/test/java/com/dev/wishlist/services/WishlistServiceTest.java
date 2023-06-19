package com.dev.wishlist.services;

import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.exceptions.NotFoundException;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.ProductCatalog;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import com.dev.wishlist.testutils.creators.ProductCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.dev.wishlist.testutils.creators.ProductCreator.createProductSetWithMaxCapacity;
import static com.dev.wishlist.testutils.creators.ProductCreator.createSingleProduct;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @InjectMocks
    WishlistService wishlistService;

    @Mock
    WishlistRepository wishlistRepository;

    @Mock
    ProductCatalogRepository productCatalogRepository;

    @Mock
    WishlistNotifierService notifier;

    final Long userId = 1L;
    final String wishlistId = "wishlistId";

    @Test
    @DisplayName("Should add product to wishlist")
    void shouldAddProductToWishlist() {
        Product product = ProductCreator.createSingleProduct();

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setName("wishlist");
        wishlist.setPublic(true);
        wishlist.setId(wishlistId);

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(wishlist));

        doNothing().when(notifier).notify(any(), anyLong(), anyLong());

        assertDoesNotThrow(() -> wishlistService.addToWishlist(product, userId, wishlistId));
        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to add a product and wishlist is full")
    void shouldThrowExceptionWhenWishlistIsFull() {

        Product product = createSingleProduct();
        Set<Product> products = createProductSetWithMaxCapacity();

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(new Wishlist(wishlistId, "Birthday wishlist", 1L, products)));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> wishlistService.addToWishlist(product, userId, wishlistId));
        assertThat(badRequestException.getMessage()).isEqualTo("It was not possible to add the selected product to the wishlist as it is already full.");
        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to add a product that was already added")
    void shouldThrowExceptionWhenProductWasAlreadyAdded() {

        Product product = createSingleProduct();
        Set<Product> products = new HashSet<>();
        products.add(product);

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(new Wishlist(wishlistId, "Birthday wishlist", 1L, products)));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> wishlistService.addToWishlist(product, userId, wishlistId));
        assertThat(badRequestException.getMessage()).isEqualTo("Product already added to wishlist.");
        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
    }

    @Test
    @DisplayName("Should find a product by its name")
    void shouldFindAProductByItsName() {

        Product product = createSingleProduct();
        ProductCatalog productCatalog = new ProductCatalog(1L, "Nike", "Nice shoes", new BigDecimal("400.00"), "https://somelink");

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(new Wishlist(wishlistId, "wishlist", userId, Set.of(product))));

        when(productCatalogRepository.findAllById(anyList()))
                .thenReturn(List.of(productCatalog));

        WishlistResponse wishlistResponse = assertDoesNotThrow(() -> wishlistService.filterProducts(userId, wishlistId, "ni"));

        assertThat(wishlistResponse.getUserId()).isEqualTo(userId);
        assertThat(wishlistResponse.getProducts()).isEqualTo(List.of(productCatalog));

        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
        verify(productCatalogRepository, times(1)).findAllById(anyList());
    }

    @Test
    @DisplayName("Should not find a product by its name and throw exception")
    void shouldNotFindAProductByItsNameAndThrowException() {
        final Long userId = 1L;
        Product product = createSingleProduct();

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(new Wishlist(wishlistId, "wishlist", userId, Set.of(product))));

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> wishlistService.filterProducts(1L, wishlistId, "mock"));
        assertThat(notFoundException.getMessage()).isEqualTo(format("Product not found with searchInput=%s, userId=%s, wishlistId=%s", "mock", userId, wishlistId));
        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAllProducts() {

        Product product = createSingleProduct();
        ProductCatalog productCatalog1 = new ProductCatalog(1L, "Nike", "Nice shoes", new BigDecimal("400.00"), "https://somelink");
        ProductCatalog productCatalog2 = new ProductCatalog(2L, "JBL", "ear bug", new BigDecimal("300.00"), "https://somelink");

        when(wishlistRepository.findByUserIdAndWishlistId(userId, wishlistId))
                .thenReturn(Optional.of(new Wishlist(wishlistId, "Birthday wishlist", userId, Set.of(product))));

        when(productCatalogRepository.findAllById(anyList()))
                .thenReturn(List.of(productCatalog1, productCatalog2));

        WishlistResponse wishlistResponse = assertDoesNotThrow(() -> wishlistService.findAllProducts(userId, wishlistId));

        assertThat(wishlistResponse.getUserId()).isEqualTo(userId);
        assertThat(wishlistResponse.getProducts()).isEqualTo(List.of(productCatalog1, productCatalog2));

        verify(wishlistRepository, times(1)).findByUserIdAndWishlistId(userId, wishlistId);
        verify(productCatalogRepository, times(1)).findAllById(anyList());
    }
}
