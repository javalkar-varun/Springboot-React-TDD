import { render } from '@testing-library/react';
import React from 'react';
import { Route, Switch } from 'react-router-dom';
import HomePage from '../pages/HomePage';
import LoginPage from '../pages/LoginPage';
import UserSignupPage from '../pages/UserSignupPage';
import UserPage from '../pages/UserPage';

function App() {
  return (
    <div>
      <div className="container">
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route path="/login" component={LoginPage} />
          <Route path="/signup" component={UserSignupPage} />
          <Route path="/:username" component={UserPage} />
        </Switch>
      </div>
    </div>
  );
}

export default App;