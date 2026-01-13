package org.example.backend.service;

import lombok.AllArgsConstructor;
import org.example.backend.exception.CustomException;
import org.example.backend.model.Book;
import org.example.backend.model.Cart;
import org.example.backend.model.CartItem;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.CartRepository;
import org.springframework. stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java. util.ArrayList;
import java.util.Optional;

/**
 * Cart Service - Сервис для работы с корзиной
 *
 * Функции:
 * - Получение/создание корзины
 * - Добавление/удаление товаров
 * - Обновление количества товаров
 * - Расчёт итоговой суммы
 */
@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    /**
     * Получить или создать корзину пользователя
     *
     * @param userId ID пользователя
     * @return Корзина пользователя
     */
    public Cart getOrCreateCart(String userId) {
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        // Создаём новую корзину
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setItems(new ArrayList<>());
        newCart.setSubtotal(BigDecimal. ZERO);
        newCart.setTax(BigDecimal.ZERO);
        newCart.setTotal(BigDecimal.ZERO);
        newCart.setCreatedAt(LocalDateTime.now());
        newCart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(newCart);
    }

    /**
     * Добавить товар в корзину
     *
     * @param userId ID пользователя
     * @param bookId ID книги
     * @param quantity Количество
     * @return Обновлённая корзина
     */
    public Cart addToCart(String userId, String bookId, int quantity) {
        if (quantity <= 0) {
            throw new CustomException("Quantity must be greater than 0", 400);
        }

        // Получаем или создаём корзину
        Cart cart = getOrCreateCart(userId);

        // Проверяем существует ли книга и в наличии
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException("Book not found", 404));

        if (!book.isInStock() || book.getQuantityInStock() < quantity) {
            throw new CustomException("Insufficient stock", 400);
        }

        // Проверяем есть ли уже такая книга в корзине
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item. getBookId().equals(bookId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Увеличиваем количество
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            if (newQuantity > book.getQuantityInStock()) {
                throw new CustomException("Insufficient stock", 400);
            }

            item.setQuantity(newQuantity);
        } else {
            // Добавляем новый товар
            CartItem newItem = new CartItem();
            newItem.setBookId(bookId);
            newItem.setBookTitle(book.getTitle());
            newItem.setImageUrl(book.getImageUrl());
            newItem.setQuantity(quantity);
            newItem.setPrice(book.getDiscountPrice() != null &&
                    book.getDiscountPrice().compareTo(BigDecimal. ZERO) > 0
                    ?  book.getDiscountPrice()
                    : book.getPrice());

            cart.getItems().add(newItem);
        }

        // Пересчитываем итоги
        cart.recalculateTotal();
        cart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    /**
     * Обновить количество товара в корзине
     *
     * @param userId ID пользователя
     * @param bookId ID книги
     * @param quantity Новое количество
     * @return Обновлённая корзина
     */
    public Cart updateCartItem(String userId, String bookId, int quantity) {
        if (quantity < 0) {
            throw new CustomException("Quantity cannot be negative", 400);
        }

        Cart cart = getOrCreateCart(userId);

        if (quantity == 0) {
            // Удаляем товар если количество = 0
            cart.getItems().removeIf(item -> item.getBookId().equals(bookId));
        } else {
            // Обновляем количество
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CustomException("Book not found", 404));

            if (quantity > book.getQuantityInStock()) {
                throw new CustomException("Insufficient stock", 400);
            }

            Optional<CartItem> item = cart.getItems().stream()
                    .filter(i -> i.getBookId().equals(bookId))
                    .findFirst();

            if (item.isPresent()) {
                item.get().setQuantity(quantity);
            } else {
                throw new CustomException("Item not found in cart", 404);
            }
        }

        // Пересчитываем итоги
        cart.recalculateTotal();
        cart.setUpdatedAt(LocalDateTime. now());

        return cartRepository.save(cart);
    }

    /**
     * Удалить товар из корзины
     *
     * @param userId ID пользователя
     * @param bookId ID книги
     * @return Обновлённая корзина
     */
    public Cart removeFromCart(String userId, String bookId) {
        Cart cart = getOrCreateCart(userId);

        cart.getItems().removeIf(item -> item.getBookId().equals(bookId));

        // Пересчитываем итоги
        cart. recalculateTotal();
        cart.setUpdatedAt(LocalDateTime.now());

        return cartRepository.save(cart);
    }

    /**
     * Очистить корзину
     *
     * @param userId ID пользователя
     */
    public void clearCart(String userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);

        if (cart.isPresent()) {
            cartRepository.delete(cart.get());
        }
    }

    /**
     * Получить количество товаров в корзине
     *
     * @param userId ID пользователя
     * @return Количество товаров
     */
    public int getCartItemCount(String userId) {
        Cart cart = getOrCreateCart(userId);
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
