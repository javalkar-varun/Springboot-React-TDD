import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { HashRouter } from 'react-router-dom';
import App from './containers/App';
import reportWebVitals from './reportWebVitals';
import { Provider } from 'react-redux'; // Provider needs to store so import createStore below
import { createStore } from 'redux';
import authReducer from './redux/authReducer';

const loggedInState = {
    id: 1,
    username: 'user1',
    displayName: 'display1',
    image: 'profile1.png',
    password: 'P4ssword',
    isLoggedIn: true
};
const store = createStore(authReducer, loggedInState);

// In redux application we can have only one store.
// And store is responsible for holding application state. It allows state to be updated via dispatch function.
ReactDOM.render(
    <Provider store={store}>
        <HashRouter>
            <App />
        </HashRouter>
    </Provider>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
