import React from 'react'
import { Route, Routes } from 'react-router-dom'
import Home from './screens/Home'
import About from './screens/About'
import Services from './screens/Services'
import Netbanking from './screens/Netbanking'
import Contact from './screens/Contact'
import Signup from './screens/Signup'

function Routing() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="/services" element={<Services />} />
      <Route path="/netbanking" element={<Netbanking />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/contact" element={<Contact />} />
    </Routes>
  )
}

export default Routing