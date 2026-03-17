import React from 'react'
import { Link } from 'react-router-dom'

function Menu() {
  return (
    <div className="menu">
      <Link to="/">Home</Link>
      <Link to="/about">About</Link>
      <Link to="/services">Services</Link>
      <Link to="/netbanking">Netbanking</Link>
      <Link to="/contact">Contact</Link>
    </div>
  )
}

export default Menu