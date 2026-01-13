import { createContext, useContext, useState, useEffect, type ReactNode } from 'react'

/**
 * AuthContext - Контекст для управления аутентификацией
 *
 * Предоставляет:
 * - Информацию о пользователе
 * - Функции login, logout, register
 */
interface User {
    id:  string
    username: string
    email: string
    role?:  string
}

interface AuthContextType {

    user: User | null
    error: string | null
    loading:  boolean
    isAuthenticated: boolean
    login:  (email: string, password: string) => Promise<boolean>
    logout: () => Promise<void>
    register: (userData: any) => Promise<boolean>
    checkAuth: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | null>(null)

interface AuthProviderProps {
    children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [user, setUser] = useState<User | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    /**
     * Проверяем авторизован ли пользователь при загрузке
     */
    useEffect(() => {
        checkAuth()
    }, [])

    /**
     * Проверить статус аутентификации
     */
    const checkAuth = async () => {
        try {
            const response = await fetch('/api/auth/me', {
                method: 'GET',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' }
            })

            if (response.ok) {
                const userData = await response.json()
                setUser(userData)
                setError(null)
            } else {
                setUser(null)
            }
        } catch (err:  any) {
            setUser(null)
        } finally {
            setLoading(false)
        }
    }

    /**
     * Регистрация
     */
    const register = async (userData: {
        username: string
        email: string
        password: string
        firstName: string
        lastName: string
    }): Promise<boolean> => {
        try {
            setError(null)
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type':  'application/json' },
                body: JSON.stringify(userData)
            })

            if (!response.ok) {
                const data = await response.json()
                throw new Error(data.error || 'Registration failed')
            }

            await checkAuth()
            return true
        } catch (err: any) {
            setError(err. message)
            return false
        }
    }

    /**
     * Логин
     */
    const login = async (email: string, password: string): Promise<boolean> => {
        try {
            setError(null)
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            })

            if (!response.ok) {
                const data = await response.json()
                throw new Error(data.error || 'Login failed')
            }

            await checkAuth()
            return true
        } catch (err: any) {
            setError(err. message)
            return false
        }
    }

    /**
     * Логаут
     */
    const logout = async () => {
        try {
            await fetch('/api/auth/logout', {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' }
            })

            setUser(null)
            setError(null)
        } catch (err: any) {
            setError(err.message)
        }
    }

    const value:  AuthContextType = {
        user,
        error,
        loading,
        isAuthenticated: !!user,
        login,
        logout,
        register,
        checkAuth
    }

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

/**
 * Хук для использования AuthContext
 */
export function useAuth(): AuthContextType {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider')
    }
    return context
}