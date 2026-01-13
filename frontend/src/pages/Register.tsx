import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import '../styles/pages/auth.css'

/**
 * Register страница
 */
export default function Register() {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: ''
    })
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')

    const { register } = useAuth()
    const navigate = useNavigate()

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e. target
        setFormData(prev => ({ ...prev, [name]:  value }))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        setError('')

        // Валидация
        if (!formData.username || !formData.email || !formData.password ||
            !formData.firstName || !formData.lastName) {
            setError('Please fill in all fields')
            return
        }

        if (formData. password !== formData.confirmPassword) {
            setError('Passwords do not match')
            return
        }

        if (formData.password.length < 6) {
            setError('Password must be at least 6 characters')
            return
        }

        setLoading(true)

        try {
            const success = await register({
                username: formData.username,
                email: formData. email,
                password: formData.password,
                firstName: formData.firstName,
                lastName: formData.lastName
            })

            if (success) {
                navigate('/books')
            } else {
                setError('Registration failed. Please try again.')
            }
        } catch (err: any) {
            setError(err.message || 'An error occurred')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h1>Create Your Account</h1>

                {error && <div className="error-message">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="firstName">First Name</label>
                            <input
                                id="firstName"
                                name="firstName"
                                type="text"
                                value={formData.firstName}
                                onChange={handleChange}
                                placeholder="John"
                                disabled={loading}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="lastName">Last Name</label>
                            <input
                                id="lastName"
                                name="lastName"
                                type="text"
                                value={formData.lastName}
                                onChange={handleChange}
                                placeholder="Doe"
                                disabled={loading}
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            name="username"
                            type="text"
                            value={formData.username}
                            onChange={handleChange}
                            placeholder="johndoe"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="john@example.com"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="••••••••"
                            disabled={loading}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="confirmPassword">Confirm Password</label>
                        <input
                            id="confirmPassword"
                            name="confirmPassword"
                            type="password"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            placeholder="••••••••"
                            disabled={loading}
                        />
                    </div>

                    <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? 'Creating account...' : 'Register'}
                    </button>
                </form>

                <p className="auth-link">
                    Already have an account? <Link to="/login">Login here</Link>
                </p>
            </div>
        </div>
    )
}