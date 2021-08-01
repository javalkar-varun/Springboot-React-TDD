const initialState = {
    id: 0,
    username: '',
    displayName: '',
    image: '',
    password: '',
    isLoggedIn: false
};

// when dispatch is called, redux calls reducer function
export default function authReducer(state = initialState, action) {
    if (action.type === 'logout-success') {
        // use javascript spread operator for copying the object values
        return { ...initialState };
    } else if (action.type === 'login-success') {
        return {
            ...action.payload,
            isLoggedIn: true
        };
    }
        return state;
    }