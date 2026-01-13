import client from './client'

/**
 * API функции для аутентификации
 */

export interface LoginResponse {
    token: string
    userId: string
    username: string
    email: string
}

export interface RegisterResponse {
    token: string
    userId: string
    username:  string
}

export async function register(data: {
    username: string
    email: string
    password: string
    firstName: string
    lastName:  string
}): Promise<RegisterResponse> {
    const response = await client.post('/auth/register', data)
    return response.data
}

export async function login(email: string, password: string): Promise<LoginResponse> {
    const response = await client.post('/auth/login', { email, password })
    return response.data
}

export async function verifyToken(): Promise<{ valid: boolean; email?: string }> {
    try {
        const response = await client.get('/auth/verify')
        return response.data
    } catch {
        return { valid: false }
    }
}