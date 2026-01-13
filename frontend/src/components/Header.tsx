
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useCart } from '../context/CartContext'
import '../styles/header.css'

/**
 * Header компонент
 *
 * Показывает:
 * - Логотип/название магазина
 * - Навигационное меню
 * - Ссылку на корзину с количеством товаров
 * - Профиль пользователя (если авторизован)
 */
export default function Header() {
    const { isAuthenticated, logout, user } = useAuth()
    const { getCartItemCount } = useCart()
    const navigate = useNavigate()
    const cartCount = getCartItemCount()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <header className="header">
            <div className="header-container">
                {/* Логотип */}
                <Link to="/" className="logo">
                    📚 BookStore
                </Link>

                {/* Поиск */}
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search books..."
                        onKeyPress={(e) => {
                            if (e.key === 'Enter') {
                                const query = (e.target as HTMLInputElement).value
                                navigate(`/search?q=${query}`)
                            }
                        }}
                    />
                </div>

                {/* Навигация */}
                <nav className="nav">
                    <Link to="/" className="nav-link">Home</Link>
                    <Link to="/books" className="nav-link">Books</Link>

                    {isAuthenticated ? (
                        <>
                            <Link to="/cart" className="nav-link cart-link">
                                🛒 Cart ({cartCount})
                            </Link>
                            <Link to="/orders" className="nav-link">Orders</Link>
                            <div className="user-menu">
                                <span className="username">{user?.username}</span>
                                <div className="dropdown">
                                    <Link to="/profile" className="dropdown-item">Profile</Link>
                                    <Link to="/favorites" className="dropdown-item">Favorites</Link>
                                    <button onClick={handleLogout} className="dropdown-item logout">
                                        Logout
                                    </button>
                                </div>
                            </div>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className="nav-link btn-login">Login</Link>
                            <Link to="/register" className="nav-link btn-register">Register</Link>
                        </>
                    )}
                </nav>
            </div>
        </header>
    )
}