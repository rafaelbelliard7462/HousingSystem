import React from 'react';
import ApplicationItem from './ApplicationItem';

export default function ApplicationList({ applications, refreshPropertyList, status} : any) {
    console.log(applications, 'frf')
    console.log(status)
  return (
    <div className='ApplicationList' style={{
      position: 'relative',
      top: '-45px'}}>
      {applications
      .filter((application) => application.status === status)
      .map((application, index) => (
        <div className='listItem mx-4 mt-4' key={index}>
          <ApplicationItem key={application.id} applicationItem={application} refreshPropertyList={refreshPropertyList}/>
        </div>
      ))}
    </div>
  );
}
