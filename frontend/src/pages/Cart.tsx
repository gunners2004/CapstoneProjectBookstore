import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useCart } from '../context/CartContext'
import '../styles/pages/cart.css'

/**
 * Cart страница
 */
export default function CartPage() {
    const { cart, removeFromCart, updateCartItem } = useCart()
    const navigate = useNavigate()

    if (! cart || cart.items.length === 0) {
        return (
            <div className="empty-cart">
                <h2>Your cart is empty</h2>
                <p>Start shopping to add items to your cart</p>
                <Link to="/books" className="btn btn-primary">
                    Continue Shopping
                </Link>
            </div>
        )
    }

    return (
        <div className="cart-page">
            <h1>Shopping Cart</h1>

            <div className="cart-container">
                {/* Товары */}
                <div className="cart-items">
                    {cart.items.map(item => (
                        <div key={item.bookId} className="cart-item">
                            <img src={item. imageUrl} alt={item.bookTitle} />

                            <div className="item-details">
                                <h3>{item.bookTitle}</h3>
                                <p>${item.price.toFixed(2)}</p>
                            </div>

                            <div className="item-quantity">
                                <button
                                    onClick={() => updateCartItem(item.bookId, item. quantity - 1)}
                                    disabled={item.quantity <= 1}
                                >
                                    −
                                </button>
                                <span>{item.quantity}</span>
                                <button
                                    onClick={() => updateCartItem(item. bookId, item.quantity + 1)}
                                >
                                    +
                                </button>
                            </div>

                            <div className="item-subtotal">
                                <strong>${(item.price * item.quantity).toFixed(2)}</strong>
                            </div>

                            <button
                                onClick={() => removeFromCart(item. bookId)}
                                className="btn-remove"
                            >
                                Remove
                            </button>
                        </div>
                    ))}
                </div>

                {/* Сумма */}
                <div className="cart-summary">
                    <h2>Order Summary</h2>
                    <div className="summary-row">
                        <span>Subtotal:</span>
                        <span>${cart.subtotal.toFixed(2)}</span>
                    </div>
                    <div className="summary-row">
                        <span>Tax (10%):</span>
                        <span>${cart.tax.toFixed(2)}</span>
                    </div>
                    <div className="summary-row total">
                        <span>Total: </span>
                        <span>${cart.total.toFixed(2)}</span>
                    </div>

                    <button
                        onClick={() => navigate('/checkout')}
                        className="btn btn-primary btn-large"
                    >
                        Proceed to Checkout
                    </button>

                    <Link to="/books" className="btn btn-secondary">
                        Continue Shopping
                    </Link>
                </div>
            </div>
        </div>
    )
}