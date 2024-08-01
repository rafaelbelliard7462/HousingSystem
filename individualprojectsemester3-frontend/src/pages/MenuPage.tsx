import React, { useEffect, useState } from 'react';
import UserApi from '../api/UserApi';
import MenuList from '../comp/MenuList';
import { Card } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
//import './MenuPage.css';
import TokenManager from '../api/TokenManager';
export default function MenuPage(props) {


    const homeOwnerMenuList = [
        {
            id: 1,
            text: "Profile",
            image: 'user',
            path: '/menu/profilePage'
        },
        {
            id: 2,
            text: "Add Property",
            image: 'add',
            path: '/menu/addProperty'
        },
        {
            id: 3,
            text: "Manage Property",
            image: 'propertyInsurance',
            path: '/'
        }, {
            id: 4,
            text: "View Applicantions",
            image: 'application',
            path: '/menu/viewApplications'
        },
        {
            id: 5,
            text: "View Revenue",
            image: 'price',
            path: '/menu/stats'
        },
        {
            id: 6,
            text: "Logout",
            image: 'logout',
            path: '/'
        }
    ]

    const homeSeekerMenuList = [
        {
            id: 1,
            text: "Profile",
            image: 'user',
            path: '/menu/profilePage#top'
        },
        {
            id: 2,
            text: "View Applicantions",
            image: 'application',
            path: '/menu/viewApplications'
        },
        {
            id: 3,
            text: "Search for properties",
            image: 'search',
            path: '/'
        }, {
            id: 4,
            text: "Logout",
            image: 'logout',
            path: '/'
        }
    ]
    
    const [menuList, SetmenuList] = useState(homeSeekerMenuList);

    useEffect(() =>{
        LoadList();
    }, []);

    
    const LoadList = () =>{
        const claims = TokenManager.getClaims();
     
        if( claims?.roles?.includes('HOMEOWNER')){
            console.log(claims.roles)
            SetmenuList(homeOwnerMenuList);
        }
        // UserApi.getUserById(claims.userId)
        // .then(data =>{
        //     if(data.role === "Homeowner"){
        //         SetmenuList(homeOwnerMenuList);
        //     }
        // })
    }

  return (
    <div className='Menu '>
      <Card style={{ border: '4px solid #7EB6E0', borderRadius: '25px', marginTop : '23px'}}>
        <Card.Body>
          <MenuList menuList={menuList} />
        </Card.Body>
      </Card>
    </div>
  )
}
