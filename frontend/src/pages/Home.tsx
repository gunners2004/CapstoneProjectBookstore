
import { Link } from 'react-router-dom'
import BookList from '../components/BookList'
import '../styles/pages/home.css'

/**
 * Home страница
 */
export default function Home() {
    return (
        <div className="home-page">
            {/* Hero секция */}
            <section className="hero">
                <div className="hero-content">
                    <h1>Welcome to BookStore</h1>
                    <p>Discover millions of books from around the world</p>
                    <Link to="/books" className="btn btn-primary btn-large">
                        Browse Books
                    </Link>
                </div>
            </section>

            {/* Избранные книги */}
            <section className="featured-section">
                <h2>Featured Books</h2>
                <BookList />
            </section>

            {/* Категории */}
            <section className="categories">
                <h2>Popular Categories</h2>
                <div className="category-grid">
                    {['Fiction', 'Mystery', 'Science', 'History', 'Biography', 'Travel'].map(
                        category => (
                            <Link
                                key={category}
                                to={`/books?genre=${category}`}
                                className="category-card"
                            >
                                {category}
                            </Link>
                        )
                    )}
                </div>
            </section>
        </div>
    )
}