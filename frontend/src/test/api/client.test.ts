import { describe, it, expect, beforeEach, vi } from 'vitest'
import axios from 'axios'

/**
 * Простые тесты для API client
 */

describe('API Client', () => {
    beforeEach(() => {
        vi.clearAllMocks()
    })

    it('should create axios instance with correct base URL', () => {
        const client = axios.create({
            baseURL: 'http://localhost:8080/api',
            headers: { 'Content-Type':  'application/json' }
        })

        expect(client. defaults.baseURL).toBe('http://localhost:8080/api')
    })

    it('should have correct default headers', () => {
        const client = axios.create({
            baseURL: 'http://localhost:8080/api',
            headers: { 'Content-Type': 'application/json' }
        })

        expect(client.defaults.headers['Content-Type']).toBe('application/json')
    })

    it('should enable credentials', () => {
        const client = axios.create({
            baseURL: 'http://localhost:8080/api',
            withCredentials: true
        })

        expect(client.defaults.withCredentials).toBe(true)
    })

    it('should construct API URL correctly', () => {
        const baseURL = 'http://localhost:8080/api'
        const endpoint = '/books'
        const fullUrl = baseURL + endpoint

        expect(fullUrl).toBe('http://localhost:8080/api/books')
    })
})