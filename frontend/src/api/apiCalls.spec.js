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
    })
})
