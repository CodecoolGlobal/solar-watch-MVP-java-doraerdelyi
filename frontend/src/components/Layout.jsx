import Navbar from './Navbar.jsx';
import { Outlet } from 'react-router-dom';

function Layout() {
    return (
        <div>
            <Navbar />
            <main className="p-4">
                <Outlet />
            </main>
        </div>
    );
}

export default Layout;