import axios from 'axios';
import * as apiCalls from './apiCalls'
describe('apiCalls', () => {

    describe('signup', () => {
        it('calls /api/1.0/users', () => {
            const mockSignup = jest.fn(); // create mock signup function
            // axios post will make real http  request using our mockSignup function
            axios.post = mockSignup;
            apiCalls.signup();
        })
    });

    describe('login', () => {
        it('calls /api/1.0/login', () => {
            const mockLogin = jest.fn();
            axios.post = mockLogin;
            apiCalls.login({ username: 'test-user', password: 'P4ssword' });
            const path = mockLogin.mock.calls[0][0];
            expect(path).toBe('/api/1.0/login');
        })
    })
});

