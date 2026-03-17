import './App.css'
import Header from './shared/Header'
import Footer from './shared/Footer'
import './index.css'
import Routing from './Routing'

function App() {
  return (
    // this is called as fragments -> tags without any content 
    <>
      <Header></Header>
      <hr></hr>
      {/* make a routing.jsx folder and render it here */}
      <Routing></Routing>
      <hr></hr>
      <Footer></Footer>
    </>
  )
}

export default App
