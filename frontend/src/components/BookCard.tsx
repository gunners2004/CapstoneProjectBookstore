
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import '../styles/book-card.css'

/**
 * BookCard компонент
 *
 * Отображает информацию о книге в виде карточки:
 * - Обложка
 * - Название и автор
 * - Цена (с/без скидки)
 * - Рейтинг
 */
interface BookCardProps {
    id: string
    title: string
    author: string
    price:  number
    discountPrice?: number
    imageUrl:  string
    averageRating: number
    ratingCount: number
    quantityInStock: number
    onAddToCart?:  () => void
    onAddToFavorites?: () => void
}

export default function BookCard({
                                     id,
                                     title,
                                     author,
                                     price,
                                     discountPrice,
                                     imageUrl,
                                     averageRating,
                                     ratingCount,
                                     quantityInStock,
                                     onAddToCart,
                                     onAddToFavorites
                                 }:  BookCardProps) {
    const { isAuthenticated } = useAuth()
    const displayPrice = discountPrice && discountPrice > 0 ? discountPrice : price
    const hasDiscount = discountPrice && discountPrice < price

    return (
        <div className="book-card">
            {/* Изображение */}
            <div className="book-image">
                <Link to={`/books/${id}`}>
                    <img src={imageUrl} alt={title} />
                </Link>
                {hasDiscount && (
                    <span className="discount-badge">
            -{Math.round((1 - (discountPrice!  / price)) * 100)}%
          </span>
                )}
            </div>

            {/* Информация */}
            <div className="book-info">
                <Link to={`/books/${id}`} className="book-title">
                    {title}
                </Link>
                <p className="book-author">{author}</p>

                {/* Рейтинг */}
                <div className="rating">
                    <span className="stars">★★★★★</span>
                    <span className="rating-value">
            {averageRating.toFixed(1)} ({ratingCount})
          </span>
                </div>

                {/* Цена */}
                <div className="price">
                    {hasDiscount && (
                        <span className="original-price">${price.toFixed(2)}</span>
                    )}
                    <span className="current-price">${displayPrice.toFixed(2)}</span>
                </div>

                {/* Статус наличия */}
                <p className={`stock ${quantityInStock > 0 ? 'in-stock' : 'out-of-stock'}`}>
                    {quantityInStock > 0 ? 'In Stock' : 'Out of Stock'}
                </p>

                {/* Кнопки действий */}
                <div className="actions">
                    {quantityInStock > 0 ?  (
                        <button
                            onClick={onAddToCart}
                            className="btn-add-cart"
                        >
                            Add to Cart
                        </button>
                    ) : (
                        <button className="btn-add-cart disabled">Out of Stock</button>
                    )}

                    {isAuthenticated && (
                        <button
                            onClick={onAddToFavorites}
                            className="btn-favorite"
                            title="Add to favorites"
                        >
                            ❤️
                        </button>
                    )}
                </div>
            </div>
        </div>
    )
}