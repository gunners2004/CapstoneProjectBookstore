import axios from 'axios'

/**
 * Axios client для API запросов (Session-based auth)
 *
 * ВАЖНЫЕ ИЗМЕНЕНИЯ для session auth:
 * 1. Удалили setAuthToken (нет JWT)
 * 2. Включили withCredentials для отправки cookies
 * 3. Сессия управляется браузером автоматически
 */

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const client = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
    // ВАЖНО: включаем отправку cookies (сессии)
    withCredentials: true
})

/**
 * Интерцептор для обработки ошибок
 */
client. interceptors.response.use(
    response => response,
    error => {
        // При 401 - перенаправляем на логин
        if (error.response?.status === 401) {
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default client