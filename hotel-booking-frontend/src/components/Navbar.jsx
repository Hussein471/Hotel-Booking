import { Link, useNavigate} from 'react-router-dom';

function Navbar() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
            navigate('/login');
        
    };

    return (
        <nav className='navbar navbar-expand navbar-dark bg-dark px-3'>
            <Link className='navbar-brand' to='/rooms'>Hotel Booking</Link>
            <Link className='nav-link text-white' to='/dashboard'>Dashboard</Link>
            <div className='ms-auto'>
                <button className='btn btn-outline-light btn-sm' onClick={handleLogout}>
                    Logout
                </button>
            </div>
        </nav>
    );
}

export default Navbar;