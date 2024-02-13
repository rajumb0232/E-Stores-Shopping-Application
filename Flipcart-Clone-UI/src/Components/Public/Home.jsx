import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();

  const orders = (event) => {
    event.preventDefault();
    navigate('/orders');
  }

  return (
    <div className='w-svw h-svh flex justify-center items-center' >
      <button onClick={orders}>Orders</button>
    </div>
  )
}
export default Home;