package com.dev.wishlist.services;

import com.dev.wishlist.dtos.WishlistResponse;
import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.exceptions.NotFoundException;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.ProductCatalog;
import com.dev.wishlist.models.ProductProjection;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import com.dev.wishlist.testutils.ProductCreator;
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

import static com.dev.wishlist.testutils.ProductCreator.createProductListWithMaxCapacity;
import static com.dev.wishlist.testutils.ProductCreator.createSingleProduct;
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
    WishlistNotifier notifier;

    @Test
    @DisplayName("Should add product to wishlist")
    void shouldAddProductToWishlist() {

        final Long userId = 1L;
        Product product = ProductCreator.createSingleProduct();

        doNothing().when(notifier).notify(anyLong(), anyLong());

        assertDoesNotThrow(() -> wishlistService.addToWishlist(product, userId));
        verify(wishlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to add a product and wishlist is full")
    void shouldThrowExceptionWhenWishlistIsFull() {

        final Long userId = 1L;
        Product product = createSingleProduct();
        Set<Product> products = createProductListWithMaxCapacity();

        when(wishlistRepository.findByUserId(1L))
                .thenReturn(Optional.of(new Wishlist(1L, products)));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> wishlistService.addToWishlist(product, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("It was not possible to add the selected product to the wishlist as it is already full.");
        verify(wishlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to add a product that was already added")
    void shouldThrowExceptionWhenProductWasAlreadyAdded() {

        final Long userId = 1L;
        Product product = createSingleProduct();
        Set<Product> products = new HashSet<>();
        products.add(product);

        when(wishlistRepository.findByUserId(1L))
                .thenReturn(Optional.of(new Wishlist(1L, products)));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> wishlistService.addToWishlist(product, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("Product already added to wishlist.");
        verify(wishlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should find a product by its name")
    void shouldFindAProductByItsName() {

        final Long userId = 1L;
        Product product = createSingleProduct();
        ProductProjection projection = new ProductProjection();
        ProductCatalog productCatalog = new ProductCatalog(1L, "Nike", "Nice shoes", new BigDecimal("400.00"), "https://somelink");

        projection.setProducts(List.of(product));

        when(wishlistRepository.findAllProductsByUserIdAndSearchInput(1L, "ni"))
                .thenReturn(Optional.of(projection));

        when(productCatalogRepository.findAllById(anyList()))
                .thenReturn(List.of(productCatalog));

        WishlistResponse wishlistResponse = assertDoesNotThrow(() -> wishlistService.findProductsInWishlist(userId, "ni"));

        assertThat(wishlistResponse.getUserId()).isEqualTo(userId);
        assertThat(wishlistResponse.getProducts()).isEqualTo(List.of(productCatalog));

        verify(wishlistRepository, times(1)).findAllProductsByUserIdAndSearchInput(userId, "ni");
        verify(productCatalogRepository, times(1)).findAllById(anyList());
    }

    @Test
    @DisplayName("Should not find a product by its name and throw exception")
    void shouldNotFindAProductByItsNameAndThrowException() {

        when(wishlistRepository.findAllProductsByUserIdAndSearchInput(1L, "ni"))
                .thenReturn(Optional.empty());

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> wishlistService.findProductsInWishlist(1L, "ni"));
        assertThat(notFoundException.getMessage()).isEqualTo("Product not found with search input: ni");
        verify(wishlistRepository, times(1)).findAllProductsByUserIdAndSearchInput(1L, "ni");
    }

}
