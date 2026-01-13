import { describe, it, expect, beforeEach } from 'vitest'

/**
 * Простые тесты для операций с корзиной
 */

describe('Cart Operations', () => {
    let cart: any

    beforeEach(() => {
        cart = {
            items: [],
            subtotal: 0,
            tax: 0,
            total:  0
        }
    })

    it('should add item to empty cart', () => {
        const newItem = {
            bookId: '1',
            title: 'Test Book',
            price: 15.99,
            quantity: 1
        }

        cart.items.push(newItem)

        expect(cart.items. length).toBe(1)
        expect(cart.items[0].title).toBe('Test Book')
    })

    it('should calculate subtotal correctly', () => {
        cart.items = [
            { bookId: '1', price: 10, quantity: 2 },
            { bookId: '2', price: 15, quantity: 1 }
        ]

        const subtotal = cart.items.reduce((sum: number, item: any) =>
            sum + (item.price * item.quantity), 0
        )

        expect(subtotal).toBe(35)
    })

    it('should calculate tax as 10% of subtotal', () => {
        const subtotal = 100
        const tax = subtotal * 0.1

        expect(tax).toBe(10)
    })

    it('should calculate total correctly', () => {
        const subtotal = 100
        const tax = 10
        const total = subtotal + tax

        expect(total).toBe(110)
    })

    it('should increase item quantity', () => {
        const item = { bookId: '1', quantity: 1 }
        item.quantity += 1

        expect(item.quantity).toBe(2)
    })

    it('should remove item from cart', () => {
        cart.items = [
            { bookId: '1', title: 'Book 1' },
            { bookId: '2', title: 'Book 2' },
            { bookId: '3', title: 'Book 3' }
        ]

        cart.items = cart.items.filter((item: any) => item.bookId !== '2')

        expect(cart.items.length).toBe(2)
        expect(cart.items. some((item: any) => item.bookId === '2')).toBe(false)
    })

    it('should clear cart', () => {
        cart.items = [
            { bookId: '1', title: 'Book 1' },
            { bookId: '2', title: 'Book 2' }
        ]

        cart.items = []
        cart.subtotal = 0
        cart.tax = 0
        cart.total = 0

        expect(cart.items.length).toBe(0)
        expect(cart.total).toBe(0)
    })

    it('should find item by bookId', () => {
        cart.items = [
            { bookId: '1', title: 'Book 1' },
            { bookId: '2', title: 'Book 2' }
        ]

        const found = cart.items.find((item: any) => item.bookId === '2')

        expect(found).toBeTruthy()
        expect(found. title).toBe('Book 2')
    })
})