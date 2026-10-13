import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../services/api'; // kama pages iko src/components/pages -> '../../services/api'

import { jwtDecode } from 'jwt-decode';


function Login() {
    const [formData, setFormData] = useState({ email: '', password: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const response = await api.post('/auth/login', formData);
            localStorage.setItem('token', response.data.token);

            const decoded = jwtDecode(response.data.token);
            localStorage.setItem('role', decoded.role);
            

            navigate('/dashboard');
        } catch (err) {
            setError(err.response?.data?.message || 'Email or password is not correct.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: '450px' }}>
            <h2 className="mb-4 text-center text-dark">Login</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Email</label>
                    <input
                        type="email"
                        className="form-control"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div className="mb-3">
                    <label className="form-label">Password</label>
                    <input
                        type="password"
                        className="form-control"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required
                    />
                </div>

                <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                    {loading ? 'Waiting...' : 'Login'}
                </button>
            </form>
            <p className="text-center mt-3">
                You don't have an account? <Link to="/register">Register</Link>
            </p>
        </div>
    );
}

export default Login;