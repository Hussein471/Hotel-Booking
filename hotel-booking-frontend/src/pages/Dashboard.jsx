import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import Layout from '../components/Layout';

function Dashboard() {
    const [bookings, setBookings] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                const [bookingsRes, roomsRes] = await Promise.all([
                    api.get('/bookings/my-bookings'),
                    api.get('/rooms'),
                ]);
                setBookings(bookingsRes.data);
                setRooms(roomsRes.data);
            } catch (err) {
                // Tunaonyesha status code halisi ili iwe rahisi ku-debug kama error ikitokea tena
                const status = err.response?.status;
                setError(`Failed to load dashboard${status ? ` (status ${status})` : ''}.`);
            } finally {
                setLoading(false);
            }
        };
        fetchDashboardData();
    }, []);

    const availableRoomsCount = rooms.filter((r) => r.available).length;
    const recentBookings = [...bookings]
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
        .slice(0, 3);

    // Rangi tofauti kwa kila summary card ili ziwe za kuvutia zaidi
    const statCards = [
        { label: 'My Bookings', value: bookings.length, icon: 'bi-calendar-check', gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { label: 'Available Rooms', value: availableRoomsCount, icon: 'bi-door-open', gradient: 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)' },
        { label: 'Total Rooms', value: rooms.length, icon: 'bi-building', gradient: 'linear-gradient(135deg, #f12711 0%, #f5af19 100%)' },
    ];

    if (loading) {
        return (
            <Layout>
                <div className="container mt-5 text-center">
                    <div className="spinner-border text-primary" role="status"></div>
                </div>
            </Layout>
        );
    }

    return (
        <Layout>
            <div className="container mt-4 mb-5">
                <h2 className="mb-4 text-dark">Dashboard</h2>
                {error && <div className="alert alert-danger">{error}</div>}

                <div className="row mb-4">
                    {statCards.map((card) => (
                        <div className="col-md-4 mb-3" key={card.label}>
                            <div
                                className="card border-0 shadow-lg text-white"
                                style={{ background: card.gradient, borderRadius: '16px' }}
                            >
                                <div className="card-body d-flex align-items-center p-4">
                                    <i className={`bi ${card.icon} fs-1 me-3`}></i>
                                    <div>
                                        <div style={{ opacity: 0.85, fontSize: '0.9rem' }}>{card.label}</div>
                                        <div className="fs-2 fw-bold">{card.value}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="mb-4">
                    <Link to="/rooms" className="btn btn-dark me-2 rounded-pill px-4">
                        <i className="bi bi-search me-2"></i>Browse Rooms
                    </Link>
                    <Link to="/my-bookings" className="btn btn-outline-dark rounded-pill px-4">
                        <i className="bi bi-list-ul me-2"></i>View All Bookings
                    </Link>
                </div>

                <div className="card border-0 shadow-sm" style={{ borderRadius: '16px' }}>
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Recent Bookings</h5>
                        {recentBookings.length === 0 ? (
                            <div className="alert alert-info mb-0">Hauna booking yoyote bado.</div>
                        ) : (
                            <ul className="list-group list-group-flush">
                                {recentBookings.map((b) => (
                                    <li key={b.id} className="list-group-item d-flex justify-content-between align-items-center px-0">
                                        <span>
                                            <i className="bi bi-door-closed me-2 text-primary"></i>
                                            {b.roomType ?? `Room #${b.roomId}`}
                                        </span>
                                        <span className="badge bg-light text-dark">
                                            {b.checkInDate} → {b.checkOutDate}
                                        </span>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                </div>
            </div>
        </Layout>
    );
}

export default Dashboard;