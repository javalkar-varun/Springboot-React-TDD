import React from 'react';
import { 
    render,
    fireEvent,
    waitForElement,
    waitFor
 } from '@testing-library/react';

import { UserSignupPage } from './UserSignupPage';

describe('UserSignupPage', () => {

    describe('Layout', () => {

        it('has header of Sign Up', () => {
            const { container} = render(<UserSignupPage />);
            const header = container.querySelector('h1');
            expect(header).toHaveTextContent('Sign Up');
        });

        it('has input for display name',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const displayNameInput = queryByPlaceholderText('Your display name');
            expect(displayNameInput).toBeInTheDocument();
        });

        it('has input for username',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const usernameInput = queryByPlaceholderText('Your username');
            expect(usernameInput).toBeInTheDocument();
        });

        it('has input for password',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const passwordInput = queryByPlaceholderText('Your password');
            expect(passwordInput).toBeInTheDocument();
        });

        it('has password type for password',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const passwordInput = queryByPlaceholderText('Your password');
            expect(passwordInput.type).toBe('password');
        });

        it('has input for password repeat',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const passwordRepeat = queryByPlaceholderText('Repeat your password');
            expect(passwordRepeat).toBeInTheDocument();
        });

        it('has password type for password repeat input',() => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const passwordRepeat = queryByPlaceholderText('Repeat your password');
            expect(passwordRepeat.type).toBe('password');
        });

        it('has submit button', () => {
            const {container } = render(<UserSignupPage />);
            const button = container.querySelector('button');
            expect(button).toBeInTheDocument();
        });
    });

    describe('Interactions', () => {
        const changeEvent = (content) => {
            return {
                target: {
                    value: content
                }
            };
        };

        const mockAsyncDelayed = () => {
            return jest.fn().mockImplementation(() => {
                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        resolve({});
                    }, 300)
                })
            })
        }

        let button, displayNameInput, usernameInput, passwordInput, passwordRepeat;

        const setupForSubmit = (props) => {

            const rendered = render(
                <UserSignupPage {...props} />
            );

            const {container, queryByPlaceholderText } = rendered;

            displayNameInput = queryByPlaceholderText('Your display name');
            usernameInput = queryByPlaceholderText('Your username');
            passwordInput = queryByPlaceholderText('Your password');
            passwordRepeat = queryByPlaceholderText('Repeat your password');

            fireEvent.change(displayNameInput, changeEvent('my-display-name'));
            fireEvent.change(usernameInput, changeEvent('my-user-name'));
            fireEvent.change(passwordInput, changeEvent('P4ssword'));
            fireEvent.change(passwordRepeat, changeEvent('P4ssword'));

            button = container.querySelector('button');
            return rendered;
        };

        it('sets the displayName value into state', () => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const displayNameInput = queryByPlaceholderText('Your display name');

            fireEvent.change(displayNameInput, changeEvent('my-display-name'));
            expect(displayNameInput).toHaveValue('my-display-name');
        });

        it('sets the username value into state', () => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const usernameInput = queryByPlaceholderText('Your username');

            fireEvent.change(usernameInput, changeEvent('my-username'));
            expect(usernameInput).toHaveValue('my-username');
        });

        it('sets the password value into state', () => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const passwordInput = queryByPlaceholderText('Your password');

            fireEvent.change(passwordInput, changeEvent('P4ssword'));
            expect(passwordInput).toHaveValue('P4ssword');
        });

        it('sets the password repeat value into state', () => {
            const { queryByPlaceholderText } = render(<UserSignupPage/>);
            const repeatPassword = queryByPlaceholderText('Repeat your password');

            fireEvent.change(repeatPassword, changeEvent('P4ssword'));
            expect(repeatPassword).toHaveValue('P4ssword');
        });

        it('calls postSignup when the fields are valid and the actions are provided in props', () => {
            const actions = {
                postSignup: jest.fn().mockResolvedValueOnce({})
            };

            setupForSubmit({actions});

            fireEvent.click(button);
            expect(actions.postSignup).toHaveBeenCalledTimes(1);
        });

        it('does not throw exception when clicking the button when actions are not provided in props', () => {
            setupForSubmit();
            expect(() => fireEvent.click(button)).not.toThrow();
        });

        it('calls post with user body when the fields are valid', () => {
            const actions = {
                postSignup: jest.fn().mockResolvedValueOnce({})
            };

            setupForSubmit({actions});
            fireEvent.click(button);
            const expectedUserObject = {
                username: 'my-user-name',
                displayName: 'my-display-name',
                password: 'P4ssword',
            }
            expect(actions.postSignup).toHaveBeenCalledWith(expectedUserObject);
        });

        it('does not allow user to click the Sign Up button when there is an ongoing api call', () => {
            const actions = {
                postSignup: mockAsyncDelayed()
            };

            setupForSubmit({actions});
            fireEvent.click(button);
            fireEvent.click(button);

            expect(actions.postSignup).toHaveBeenCalledTimes(1);
        });

        it('displays spinner when there is an ongoing api call', () => {
            const actions = {
                postSignup: mockAsyncDelayed()
            };

            const {queryByText} = setupForSubmit({ actions });
            fireEvent.click(button);
            const spinner = queryByText('Loading...'); // loading ... is coming from bootstrap spinner component

            expect(spinner).toBeInTheDocument();
        });

        it('hide spinner after api call finishes successfully',async () => {
            const actions = {
                postSignup: mockAsyncDelayed()
            };

            const {queryByText} = setupForSubmit({ actions });
            fireEvent.click(button);
            await waitFor(() => expect(queryByText('Loading...')).not.toBeInTheDocument());

            // deprecated
  //const spinner = queryByText('Loading...'); // loading ... is coming from bootstrap spinner component

            //expect(spinner).not.toBeInTheDocument();
        });

        it('hide spinner after api call finishes with error',async () => {
            const actions = {
                postSignup: jest.fn().mockImplementation(() => {
                    return new Promise((resolve, reject) => {
                        setTimeout(() => {
                            reject({
                                response: {data: {}}
                            });
                        }, 300);
                    });
                })
            };

            const {queryByText} = setupForSubmit({ actions });
            fireEvent.click(button);
            await waitFor(() => expect(queryByText('Loading...')).not.toBeInTheDocument());

            //const spinner = queryByText('Loading...'); // loading ... is coming from bootstrap spinner component

            //expect(spinner).not.toBeInTheDocument();
        });

        it('display validation error for displayName when error is received for the field', async () => {
            const actions = {
                postSignup: jest.fn().mockRejectedValue({
                    response: {
                        data: {
                            validationErrors: {
                                displayName: 'Cannot be null'
                            }
                        }
                    }
                })
            }
            const { queryByText } = setupForSubmit({ actions });
            fireEvent.click(button);

            // const errorMessage = await waitForElement(() => queryByText('Cannot be null'));
            
             await waitFor(() => expect(queryByText('Cannot be null')).toBeInTheDocument());
            // expect(errorMessage).toBeInTheDocument();
        });

        it('enables the signup button when password and repeat password have same value', () => {
            setupForSubmit();
            expect(button).not.toBeDisabled();
        });

        it('disables the signup button when password repeat does not match to password', () => {
            setupForSubmit();
            fireEvent.change(passwordRepeat, changeEvent('new-pass'));
            expect(button).toBeDisabled();
        });

        it('disables the signup button when password does not match to password repeat', () => {
            setupForSubmit();
            fireEvent.change(passwordInput, changeEvent('new-pass'));
            expect(button).toBeDisabled();
        });

        it('display error style for password repeat input when password repeat mismatch', () => {
            const { queryByText } = setupForSubmit();
            fireEvent.change(passwordRepeat, changeEvent('new-pass'));
            const mismatchWarning = queryByText('Does not match to password');
            expect(mismatchWarning).toBeInTheDocument();
        });

        it('hides the validation error when user changes the content of displayName', async () => {
            const actions = {
                postSignup: jest.fn().mockRejectedValue({
                    response: {
                        data: {
                            validationErrors: {
                                displayName: 'Cannot be null'
                            }
                        }
                    }
                })
            }
            // we have to query the error message so have queryByText
            const { queryByText } = setupForSubmit({ actions });
            fireEvent.click(button);
            
            await waitFor(() => expect(queryByText('Cannot be null')).toBeInTheDocument());
            fireEvent.change(displayNameInput, changeEvent('name updated'));

            const errorMessage = queryByText('Cannot be null');
            expect(errorMessage).not.toBeInTheDocument();
        });

        it('hides the validation error when user changes the content of username', async () => {
            const actions = {
                postSignup: jest.fn().mockRejectedValue({
                    response: {
                        data: {
                            validationErrors: {
                                username: 'Username cannot be null'
                            }
                        }
                    }
                })
            }
            // we have to query the error message so have queryByText
            const { queryByText } = setupForSubmit({ actions });
            fireEvent.click(button);
            
            // After clicking the button we will for "username cannot be null" to be shown
            // then we change username input to "name update"
            await waitFor(() => expect(queryByText('Username cannot be null')).toBeInTheDocument());
            fireEvent.change(usernameInput, changeEvent('name updated'));

            const errorMessage = queryByText('Username cannot be null');
            expect(errorMessage).not.toBeInTheDocument();
        });

        it('hides the validation error when user changes the content of password', async () => {
            const actions = {
                postSignup: jest.fn().mockRejectedValue({
                    response: {
                        data: {
                            validationErrors: {
                                password: 'Cannot be null'
                            }
                        }
                    }
                })
            }
            // we have to query the error message so have queryByText
            const { queryByText } = setupForSubmit({ actions });
            fireEvent.click(button);
            
            await waitFor(() => expect(queryByText('Cannot be null')).toBeInTheDocument());
            fireEvent.change(passwordInput, changeEvent('updated-password'));

            const errorMessage = queryByText('Cannot be null');
            expect(errorMessage).not.toBeInTheDocument();
        });
    });
});

console.error = () => {};
