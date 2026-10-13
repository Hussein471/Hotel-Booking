import { useEffect, useState } from "react";
import api from '../services/api';
import Layout from "../components/Layout";


function AdminDashboard() {

    const [rooms, setRooms] = useState([]);
    const [bookings, setBookings] = useState([]);
    const [error, setError] = useState(['']);
    const [loading, setLoading] = useState(true);

    const [newRoom, setNewRoom] = useState({ roomType: '', roomNumber: '', pricePerNight: ''});

    const loadData = async () => {
        try {
            const [roomsRes, bookingsRes] = await Promise.all([
                api.get('/rooms'),
                api.get('/admin/bookings'),
            ]);
            setRooms(roomsRes.data);
            setBookings(bookingsRes.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to load admin data.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        loadData();
    }, []);

    const handleAddRoom = async (e) => {
        e.preventDefault();
        try {
            await api.post('/rooms', {
                ...newRoom,
                pricePerNight: Number(newRoom.pricePerNight),
                available: true,
            });
            setNewRoom({ roomType: '', roomNumber: '', pricePerNight: '' });
            loadData(); 
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to add room.');
        }
    };

    const handleDeleteRoom = async (id) => {
        if (!window.confirm('Are you sure you to delete this room?')) return;
        try {
            await api.delete(`/rooms/${id}`);
            loadData();
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to delete room.');
        }
    };


    if (loading) {
        return (
            <Layout>
                <div className="container mt-5 text-center">
                    <div className="spinner-border ext-primary" role="status"></div>
                </div>
            </Layout>
        );
    }

    return (
        <Layout>
            <div className="container mt-4 mb-5">
                <h2 className="mb-4 fw-bold text-dark">Admin Panel</h2>
                {error && <div className="alert alert-danger">{error}</div>}


                {/*form ya kuongeza room mpya */}
                <div className="card border-0 shadow-sm mb-4" style={{ borderRadius: '16px'}}>
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">Add New Room</h5>
                        <form onSubmit={handleAddRoom} className="row g-2">
                            
                            <div className="col-md-3">
                                <input
                                    className="form-control"
                                    placeholder="Room Number"
                                    value={newRoom.roomNumber}
                                    onChange={(e) => setNewRoom({ ...newRoom, roomNumber: e.target.value })}
                                    required
                                    />
                            </div>
                            
                            
                            <div className="col-md-3">
                                
                                
                                <select
                                className="form-control"
                                placeholder="Room Type"
                                
                                value={newRoom.roomType}
                                onChange={(e) => setNewRoom({...newRoom, roomType: e.target.value })}
                                required
                                > 
                                <option value="single">SINGLE</option>
                                <option value="double">DOUBLE</option>
                                <option value="suite">SUITE</option>
                                <option value="self">SELF</option>

                                </select>
                            </div>
                            <div className="col-md-3">
                                <input
                                 type="number"
                                 className="form-control"
                                 placeholder="Price per night"
                                 value={newRoom.pricePerNight}
                                 onChange={(e) => setNewRoom({...newRoom, pricePerNight: e.target.value}) }
                                 required
                                 />

                            </div>
                            <div className="col-md-3">
                                <button type="submit" className="btn btn-primary w-100">
                                    <i className="bi bi-plus-lg mw-1"></i>Add Room
                                </button>
                            </div>
                        </form>
                    </div>

                </div>


                {/*orosha ya room zote na button za kufuta */}
                <div className="card border-0 shadow-sm mb-4" style={{ borderRadius: '16px'}}>
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">All Rooms ({rooms.length})</h5>
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>Type</th>
                                    <th>Number</th>
                                    <th>Price</th>
                                    <th>Status</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {rooms.map((r) => (
                                    <tr key={r.id}>
                                        <td>{r.roomType}</td>
                                        <td>{r.roomNumber}</td>
                                        <td>{Number(r.pricePerNight).toLocaleString()}</td>
                                        <td>
                                            <span className={`badge ${r.available ? 'bg-success' : 'bg-secondary'}`}>
                                                {r.available ? 'Available' : 'Booked'}
                                            </span>
                                        </td>
                                        <td>
                                            <button
                                                className="btn btn-sm btn-outline-danger"
                                                onClick={() => handleDeleteRoom(r.id)}
                                                >
                                                    <i className="bi bi-trash"></i>
                                                </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                {/*orodha ya booking */}
                <div className="card border-0 shadow-sm" style={{ borderRadius: '16px'}}>
                    <div className="card-body p-4">
                        <h5 className="fw-bold mb-3">All Bookings ({bookings.length})</h5>
                        <table className="table">
                            <thead>
                                <tr>
                                    <th>User</th>
                                    <th>Room</th>
                                    <th>Check-in</th>
                                    <th>Check-out</th>
                                </tr>
                            </thead>
                            <tbody>
                                {bookings.map((b) => (
                                    <tr key={b.id}>
                                        <td>{b.userName} ({b.userEmail})</td>
                                        <td>{b.roomType} - {b.roomNumber}</td>
                                        <td>{b.checkInDate}</td>
                                        <td>{b.checkOutDate}</td>
                                    </tr>
                                ) )}
                            </tbody>
                        </table>
                    </div>
                </div>



            </div>
        </Layout>
    );


    
}
export default AdminDashboard;