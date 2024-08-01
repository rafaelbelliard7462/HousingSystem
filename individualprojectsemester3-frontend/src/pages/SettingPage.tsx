import React from 'react'

import {  Button } from "react-bootstrap";
import TokenManager from '../api/TokenManager';
export default function SettingPage() {
  return (
    <div>SettingPage
      <Button variant='primary' onClick={()=>{TokenManager.clear()}}>Logout</Button>
    </div>
  )
}
