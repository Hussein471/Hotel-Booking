import { Link, useNavigate } from 'react-router-dom';

function Sidebar({ isOpen, onClose }) {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const role = localStorage.getItem('role');

    const menuItems = [
        {to: '/dashboard', label: 'Dashboard', icon: 'bi-speedometer2'},
        {to: '/rooms', label: 'Rooms', icon: 'bi-door-open' },
        {to: '/my-bookings', label: 'My Bookings', icon: 'bi-calender-check'},

        ...(role === 'ADMIN' ? [{ to: '/admin', label: 'Admin Panel', icon: 'bi-shield-lock'}] : []),
    ];

    return (
        <>
        
        {isOpen && (

            <div
                onClick={onClose}
                    style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        backgroundColor: 'rgba(0,0,0,0.5)',
                        zIndex: 1040,
                    }}
            />

        )}

        <div
        
            style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    height: '100vh',
                    width: '260px',
                    background: 'linear-gradient(180deg, #1a1a2e 0%, #16213e 100%)',
                    transform: isOpen ? 'translateX(0)' : 'translateX(-100%)',
                    transition: 'transform 0.3s ease-in-out',
                    zIndex: 1050,
                    boxShadow: '4px 0 15px rgba(0,0,0,0.3)',
                }}
        >

                <div className='p-4 border-bottom border-secondary'>
                    <h4 className='text-white mb-0'>
                        <i className='bi bi-building me-2'></i>
                        Hotel Booking
                    </h4>
                </div>

                <nav className='p-3'>
                    {menuItems.map((item) => (
                        <Link
                            key={item.to}
                            to={item.to}
                            onClick={onClose}
                            className='d-flex align-items-center text-white text-decoration-none p-3 mb-2 rounded'
                            style={{
                                transition: 'background 0.2s',
                            }}
                            onMouseEnter={(e) => (e.currentTarget.style.background = 'rgba(255,255,255,0.1)')}
                            onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
                            >


                            <i className={`bi ${item.icon} me-3 fs-5`}></i>
                            {item.label}
                            </Link>

                    ))}

                    <hr className='border-secondary' />

                    <button 
                        onClick={handleLogout}
                        className='d-flex align-items-center text-white text-decoration-none p-3 rounded w-100 border-0 bg-transparent'
                        style={{ transition: 'background 0.2s'}}
                        onMouseEnter={(e) => (e.currentTarget.style.background = 'rgba(220,53,69,0.3)')}
                        onMouseLeave={(e) => (e.currentTarget.style.background = 'transparent')}
                        >

                            <i className='bi bi-box-arrow-right me-3 fs-5'></i>
                            Logout
                        </button>
                </nav>

        </div>
            
        </>
    );
}
export default Sidebar;