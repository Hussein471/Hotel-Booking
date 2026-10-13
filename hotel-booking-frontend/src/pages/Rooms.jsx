import { useEffect, useState } from 'react';
import api from '../services/api';
import Layout from '../components/Layout';
import { useNavigate } from 'react-router-dom';


function Rooms() {
    const [rooms, setRooms] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRooms = async () => {
            try {
                const response = await api.get('/rooms');
                setRooms(response.data);
            } catch (err) {
                setError(err.response?.data?.message || 'Failed to load rooms.');
            } finally {
                setLoading(false);
            }
        };
        fetchRooms();
    }, []);

    return (
        <>
            <Layout>
            <div className="container mt-4">
                <h2 className="mb-4 text-dark">Available Rooms</h2>

                {loading && <div className="text-center">Loading...</div>}
                {error && <div className="alert alert-danger">{error}</div>}
                {!loading && !error && rooms.length === 0 && (
                    <div className="alert alert-info">No rooms found.</div>
                )}

                <div className="row">
                    {rooms.map((room) => (
                        <div className="col-md-4 mb-4" key={room.id}>
                            <div className="card h-100 shadow-sm">
                                <div className="card-body">
                                    <h5 className="card-title">{room.roomType}</h5>
                                    <p className="card-text mb-1">Room No: {room.roomNumber}</p>
                                    <p className="card-text mb-2">
                                        {Number(room.pricePerNight).toLocaleString()} / night
                                    </p>
                                    <span className={`badge ${room.available ? 'bg-success' : 'bg-secondary'}`}>
                                        {room.available ? 'Available' : 'Booked'}

                                        
                                    </span>

                                    {room.available && (
                                            <button 
                                            className='btn btn-primary btn-sm mt-2 w-100'
                                            onClick={() => navigate(`/book/${room.id}`)}
                                            >
                                                Book Now
                                            </button>
                                        )}
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            </Layout>
        </>
    );
}

export default Rooms;