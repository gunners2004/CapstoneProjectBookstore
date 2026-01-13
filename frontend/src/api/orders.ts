import client from './client'

/**
 * API функции для заказов
 */

export interface Order {
    id: string
    orderNumber: string
    items: any[]
    total: number
    status: string
    createdAt: string
    shippedDate?: string
    deliveredDate?: string
}

export async function createOrder(data: any): Promise<Order> {
    const response = await client.post('/orders', data)
    return response.data
}
