<template>
  <div class="header">
    <header id="header">
      <span>Shornikov Sergei P3211 Variant ...</span>
    </header>
  </div>
  <div class="auth-page">
    <div class="form-container" v-if="showLogin">
      <form @submit.prevent="login">
        <input type="text" v-model="username" placeholder="Username" required>
        <input type="password" v-model="password" placeholder="Password" required>
        <button type="submit">Login</button>
        <p v-if="loginError" class="error-message">Неправильный логин или пароль</p>
      </form>
      <p class="toggle-form" @click="toggleForm">Нет аккаунта?</p>
    </div>

    <div class="form-container" v-else>
      <form @submit.prevent="register">
        <input type="text" v-model="newUsername" placeholder="Username" required>
        <input type="password" v-model="newPassword" placeholder="Password" required>
        <button type="submit">Register</button>
        <p v-if="registrationError" class="error-message">Такой пользователь уже существует</p>
      </form>
      <p class="toggle-form" @click="toggleForm">Уже зарегистрированы?</p>
    </div>
  </div>
</template>

<script>
import Toastify from 'toastify-js'
import 'toastify-js/src/toastify.css'
export default {
  name: 'AuthPage',
  data() {
    return {
      showLogin: true,
      username: '',
      password: '',
      newUsername: '',
      newPassword: '',
      loginError: false,
      registrationError: false,
    };
  },
  methods: {
    toggleForm() {
      this.showLogin = !this.showLogin;
      this.loginError = false;
      this.registrationError = false;
    },
    login() {
      fetch("http://localhost:8080/api/users/login", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: this.username,
          password: this.password
        })
      })
          .then(response => {
            if (!response.ok) {
              console.log(response.status);
              throw new Error('Login failed');
            }
            return response.text();
          })
          .then(token => {
            sessionStorage.setItem('jwtToken', token);
            this.$router.push('/main');
          })
          .catch(error => {
            console.log(error);
            this.loginError = true;
          })
    },
    register() {
      fetch("http://localhost:8080/api/users/register", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: this.newUsername,
          password: this.newPassword
        })
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Registration failed');
            }
            Toastify({
              close: true,
              text: "Registration successful!. \nPlease sign in",
              duration: 3000,
              style: {
                color: 'white',
                background: "linear-gradient(to right, #489149, #539680)",
              },
            }).showToast();

          })
          .catch(error => {
            console.log(error);
            this.registrationError = true;

          })
    }
  }
};
</script>

<style scoped>

.header {
  background: white;
  width: 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 10px 0;
  text-align: center;
  font-family: 'Arial', sans-serif;
  margin-bottom: 20px;
}

#header span {
  color: #333;
  font-size: 1.2em;
}

.auth-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(to right, #74ebd5, #9face6);
  font-family: 'Arial', sans-serif;
}

.form-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 500px;
  margin: 20px;
}

input[type="text"],
input[type="password"] {
  width: calc(100% - 20px);
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

button {
  margin: 10px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
}

button:hover {
  background-color: #45a049;
}

.toggle-form {
  color: #4CAF50;
  cursor: pointer;
  margin-top: 10px;
  transition: color 0.3s ease;
}

.toggle-form:hover {
  color: #388e3c;
  text-decoration: underline;
}
.error-message {
  color: red;
  font-size: 0.9em;
  margin-top: 10px;
}

@media (min-width: 1212px) {
  .auth-page .form-container {
    width: 35%;
  }
}

@media (min-width: 876px) and (max-width: 1211px) {
  .auth-page .form-container {
    width: 50%;
  }
}

@media (max-width: 875px) {
  .auth-page .form-container {
    width: 75%;
  }
}
</style>
