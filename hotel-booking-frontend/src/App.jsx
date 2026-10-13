import { BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Rooms from './pages/Rooms';
import ProtectedRoute from './components/ProtectedRoute';
import BookRoom from './pages/BookRoom';
import MyBookings  from './pages/MyBookings';
import Dashboard from './pages/Dashboard';
import AdminDashboard from './pages/AdminDashboard';
import AdminRoute from './components/AdminRoute';

function App() {
  return (
    <BrowserRouter>
    <Routes>
      <Route path='/' element={<Navigate to="/login" />} />
      <Route path='/login' element={<Login />} />
      <Route path='/register' element={<Register />} />
      <Route 
      path='/rooms' element={<ProtectedRoute>
        <Rooms />
      </ProtectedRoute>}
      />

      <Route 
        path="/book/:roomId"
        element={
          <ProtectedRoute>
            <BookRoom />
          </ProtectedRoute>
        }
        />

        <Route
          path='/my-bookings'
          element={
            <ProtectedRoute>
              <MyBookings />
            </ProtectedRoute>
          }
          />

          <Route
          path='/dashboard'
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
          />

          <Route
          path='/admin'
          element={
            <AdminRoute>
              <AdminDashboard />
            </AdminRoute>
          }
          />
          
      </Routes>
      </BrowserRouter>
  );
}

export default App;