package com.dev.wishlist.services;

import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.WishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;
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
    Wishlist wishlist;

    @Test
    @DisplayName("Should add product to wishlist")
    void shouldAddProductToWishlist() {

        final Long userId = 1L;
        Product product = new Product(1L, "Fridge", "Top notch", new BigDecimal("3.999"), "https:somelink");

        assertDoesNotThrow(() -> wishlistService.addToWishlist(product, userId));
        verify(wishlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should throw exception when user tries to add a product and wishlist is full")
    void shouldThrowExceptionWhenWishlistIsFull() {

        final Long userId = 1L;
        Product product = new Product(1L, "Fridge", "Top notch", new BigDecimal("3.999"), "https:somelink");
        List<Product> products = createProductListWithMaxCapacity();

        when(wishlistRepository.findByUserId(1L))
                .thenReturn(Optional.of(new Wishlist(1L, products)));

        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> wishlistService.addToWishlist(product, userId));
        assertThat(badRequestException.getMessage()).isEqualTo("It was not possible to add the selected product to the wishlist as it is already full.");
        verify(wishlistRepository, times(1)).findByUserId(userId);
    }

    private List<Product> createProductListWithMaxCapacity() {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < MAX_WISHLIST_SIZE; i++) {
            products.add(new Product(1L, "Fridge", "Top notch", new BigDecimal("3.999"), "https:somelink"));
        }

        return products;
    }
}
