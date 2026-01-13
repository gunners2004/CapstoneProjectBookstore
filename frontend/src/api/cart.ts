import client from './client'

/**
 * API функции для корзины
 */

export interface CartItem {
    bookId: string
    bookTitle: string
    imageUrl: string
    quantity: number
    price: number
}

export interface Cart {
    id: string
    items: CartItem[]
    subtotal: number
    tax: number
    total: number
}

export async function getCart(): Promise<Cart> {
    const response = await client.get('/cart')
    return response.data
}

export async function addToCart(bookId: string, quantity:  number): Promise<Cart> {
    const response = await client.post('/cart/add', { bookId, quantity })
    return response.data
}

export async function updateCartItem(bookId: string, quantity: number): Promise<Cart> {
    const response = await client.put(`/cart/items/${bookId}`, { quantity })
    return response.data
}

export async function removeFromCart(bookId: string): Promise<Cart> {
    const response = await client.delete(`/cart/items/${bookId}`)
    return response.data
}

export async function clearCart(): Promise<void> {
    await client.delete('/cart')
}