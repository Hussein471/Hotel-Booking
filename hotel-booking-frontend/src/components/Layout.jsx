import {useState} from  'react';
import Sidebar from './Sidebar';


function Layout({ children }) {
 
    const [sidebarOpen, setSidebarOpen] = useState(false);

    return (
        <div>
            <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />


            <nav
            className='navbar px-3'
            style={{background: 'linear-gradient(90deg, #1a1a2e 0%, #16213e 100%)'}}
            >

<button
    className='btn text-white border-0 fs-4'
    onClick={() => setSidebarOpen(true)}
>
    <i className='bi bi-list'></i>
</button>
<span className='text-white fw-bold'>Hotel Booking System</span>
<span style={{ width: '40px' }}></span>

            </nav>

        <div >{children}</div>

        </div>
    );
}
export default Layout;