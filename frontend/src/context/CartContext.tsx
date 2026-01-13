import React, { createContext, useContext, useState, useEffect } from 'react'
import * as cartAPI from '../api/cart'
import { useAuth } from './AuthContext'

/**
 * CartContext - Контекст для управления корзиной
 */
interface CartItem {
    bookId: string
    bookTitle: string
    imageUrl:  string
    quantity: number
    price: number
}

interface Cart {
    id: string
    items: CartItem[]
    subtotal: number
    tax: number
    total: number
}

interface CartContextType {
    cart: Cart | null
    loading: boolean
    error: string | null
    addToCart: (bookId: string, quantity: number) => Promise<boolean>
    removeFromCart: (bookId: string) => Promise<boolean>
    updateCartItem: (bookId: string, quantity: number) => Promise<boolean>
    clearCart: () => Promise<void>
    refreshCart: () => Promise<void>
    getCartItemCount: () => number
}

const CartContext = createContext<CartContextType | null>(null)

export function CartProvider({ children }: { children: React.ReactNode }) {
    const [cart, setCart] = useState<Cart | null>(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const { isAuthenticated } = useAuth()

    /**
     * Загрузить корзину при логине
     */
    useEffect(() => {
        if (isAuthenticated) {
            refreshCart()
        }
    }, [isAuthenticated])

    /**
     * Загрузить корзину с сервера
     */
    const refreshCart = async () => {
        if (!isAuthenticated) return

        try {
            setLoading(true)
            const data = await cartAPI.getCart()
            setCart(data)
            setError(null)
        } catch (err: any) {
            setError(err.message)
        } finally {
            setLoading(false)
        }
    }

    /**
     * Добавить товар в корзину
     */
    const addToCart = async (bookId: string, quantity:  number): Promise<boolean> => {
        try {
            setError(null)
            const data = await cartAPI.addToCart(bookId, quantity)
            setCart(data)
            return true
        } catch (err: any) {
            setError(err.message)
            return false
        }
    }

    /**
     * Удалить товар из корзины
     */
    const removeFromCart = async (bookId: string): Promise<boolean> => {
        try {
            setError(null)
            const data = await cartAPI. removeFromCart(bookId)
            setCart(data)
            return true
        } catch (err: any) {
            setError(err.message)
            return false
        }
    }

    /**
     * Обновить количество товара
     */
    const updateCartItem = async (bookId: string, quantity: number): Promise<boolean> => {
        try {
            setError(null)
            const data = await cartAPI.updateCartItem(bookId, quantity)
            setCart(data)
            return true
        } catch (err: any) {
            setError(err.message)
            return false
        }
    }

    /**
     * Очистить корзину
     */
    const clearCart = async () => {
        try {
            setError(null)
            await cartAPI.clearCart()
            setCart(null)
        } catch (err: any) {
            setError(err.message)
        }
    }

    /**
     * Получить количество товаров в корзине
     */
    const getCartItemCount = (): number => {
        if (! cart || !cart.items) return 0
        return cart. items.reduce((total, item) => total + item.quantity, 0)
    }

    const value: CartContextType = {
        cart,
        loading,
        error,
        addToCart,
        removeFromCart,
        updateCartItem,
        clearCart,
        refreshCart,
        getCartItemCount
    }

    return (
        <CartContext.Provider value={value}>
            {children}
        </CartContext.Provider>
    )
}

/**
 * Хук для использования CartContext
 */
export function useCart(): CartContextType {
    const context = useContext(CartContext)
    if (!context) {
        throw new Error('useCart must be used within CartProvider')
    }
    return context
}