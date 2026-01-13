import { describe, it, expect } from 'vitest'

/**
 * Простые тесты для функций валидации
 */

describe('Validation Functions', () => {
    // Email validation
    const validateEmail = (email: string) => {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
    }

    it('should validate correct email', () => {
        expect(validateEmail('user@example.com')).toBe(true)
    })

    it('should reject email without @', () => {
        expect(validateEmail('userexample.com')).toBe(false)
    })

    it('should reject email without domain', () => {
        expect(validateEmail('user@')).toBe(false)
    })

    // Password validation
    const validatePassword = (password: string) => {
        return password.length >= 6
    }

    it('should accept password >= 6 characters', () => {
        expect(validatePassword('password123')).toBe(true)
    })

    it('should reject password < 6 characters', () => {
        expect(validatePassword('pass')).toBe(false)
    })

    // Username validation
    const validateUsername = (username: string) => {
        return username.length >= 3 && username.length <= 50
    }

    it('should validate username between 3-50 characters', () => {
        expect(validateUsername('john_doe')).toBe(true)
    })

    it('should reject too short username', () => {
        expect(validateUsername('ab')).toBe(false)
    })

    // Price validation
    const validatePrice = (price: number) => {
        return price > 0 && price <= 10000
    }

    it('should validate reasonable price', () => {
        expect(validatePrice(19.99)).toBe(true)
    })

    it('should reject negative price', () => {
        expect(validatePrice(-10)).toBe(false)
    })

    it('should reject zero price', () => {
        expect(validatePrice(0)).toBe(false)
    })

    // Quantity validation
    const validateQuantity = (quantity: number) => {
        return quantity > 0 && quantity <= 100
    }

    it('should validate reasonable quantity', () => {
        expect(validateQuantity(5)).toBe(true)
    })

    it('should reject zero quantity', () => {
        expect(validateQuantity(0)).toBe(false)
    })

    it('should reject quantity > 100', () => {
        expect(validateQuantity(150)).toBe(false)
    })
})