import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from '../services/api';
import Navbar from "../components/Navbar";  


function BookRoom() {
    const {roomId} = useParams();
    const navigate = useNavigate();

    const [checkInDate, setCheckInDate] = useState('');
    const [checkOutDate, setCheckOutDate] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    console.log('roomId kutoka URL:', roomId, '-> Number:', Number(roomId));
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (checkOutDate <= checkInDate) {
            setError('Check-out date must after check-in date')
            return;
        }
        setLoading(true);
        try{
            await api.post('/bookings', {
                roomId: Number(roomId),
                checkInDate,
                checkOutDate,
            });
            navigate('/my-bookings');
        } catch (err) {
            setError(err.response?.data?.message || 'Booking failed.');
        } finally {
            setLoading(false);
        }
    };

    
    return (
        <>
        <Navbar />
        <div className="container mt-5" style={{ maxWidth: '450px'}}>
            <h2 className="mb-4">Book Room #{roomId}</h2>
            {error && <div className="alert alert-danger">{error}</div>}
            <form onSubmit={handleSubmit}>
                <div className="mb-3">
                    <label className="form-label">Check-in Date</label>
                    <input 
                        type="date"
                        className="form-control"
                        value={checkInDate}
                        onChange={(e) => setCheckInDate(e.target.value)}
                        required
                        />
                </div>

                <div className="mb-3">
                    <label className="form-label">Check-out Date</label>
                    <input 
                        type="date"
                        className="form-control"
                        value={checkOutDate}
                        onChange={(e) => setCheckOutDate(e.target.value)}
                        required
                        />
                </div>
                
                <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                    {loading ? 'Sending...' : 'Confirm Booking'}
                </button>
            </form>

        </div>
        </>
    );
}

export default BookRoom;