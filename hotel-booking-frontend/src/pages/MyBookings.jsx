import { useEffect, useState } from "react";
import api from '../services/api';
import Layout from '../components/Layout';

function MyBookings() {
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);


    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const response = await api.get('/bookings/my-bookings');
                setBookings(response.data);
            } catch (err) {
                setError(err.response?.data?.message || 'Failed to load booking.');
            } finally {
                setLoading(false);
            }
        };
        fetchBookings();
    }, []);

    return (
        <>
        
            <Layout>
            <div className="container mt-4">
                <h2 className="mb-4 text-dark">My Bookings</h2>
                {loading && <div className="text-center">Loading...</div>}
                {error && <div className="alert alert-danger">{error}</div>}
                {!loading && !error && bookings.length === 0 && (
                    <div className="alert alert-info">No bookings yet.</div>
                )}


                <ul className="list-group">
                    {bookings.map((b) => (
                        <li key={b.id} className="list-group-item">


                            Room: {b.roomType ?? b.roomId} - {b.checkInDate} to {b.checkOutDate}
                            
                        </li>
                    ))}
                </ul>


            </div>

            </Layout>

        </>
    );

}

export default MyBookings;