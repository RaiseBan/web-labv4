<template>

  <div class="sendForm">
    <form @submit.prevent>
      <div id="choose-x">
        X: {{ x }}
        <button
            v-for="num in xNumbers"
            :key="'x-' + num"
            :class="{selected: num === x}"
            @click="x = num"
        > {{ num }}
        </button>
      </div>
      <div id="choose-y">
        Y: {{ y }}
        <input
            placeholder="-3...3"
            maxlength="6"
            type="text"
            v-model="y"
        >
      </div>
      <div id="choose-r">
        R: {{ r }}
        <button type="submit"
                v-for="num in rNumbers"
                :key="'r-' + num"
                :class="{ selected: num === r }"
                @click="r = num">{{ num }}
        </button>
      </div>

      <button @click="submitForm">Отправить данные</button>
    </form>
  </div>
</template>

<script>
import Toastify from 'toastify-js'
import 'toastify-js/src/toastify.css'
export default {

  name: "InputFields",
  data() {
    return {
      x: null,
      y: '',
      r: 2,
      xNumbers: [-5, -4, -3, -2, -1, 0, 1, 2, 3],
      rNumbers: [-5, -4, -3, -2, -1, 0, 1, 2, 3]
    }
  },
  methods: {
    submitForm() {

      if (this.x === null || this.x === "") {
        Toastify({
          text: "Значение x не введено!",
          duration: 3000
        }).showToast();
        return;
      }
      if (isNaN(this.y) || this.y > 3 || this.y < -3 || this.y === null || this.y === "") {
        Toastify({
          text: "Значение Y некорректно! \n Y[-3...3]! ",
          duration: 3000
        }).showToast();
        return;
      }
      if (this.r === null || this.r < 1) {
        Toastify({
          text: "Значение R некорректно! \nR > 0! ",
          duration: 3000
        }).showToast();
        return;
      }

      const jwtToken = sessionStorage.getItem('jwtToken');
      const formData = {
        x: this.x,
        y: parseFloat(this.y),
        r: this.r
      };

      fetch('http://localhost:8080/api/shots', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          "Authorization": `Bearer ${jwtToken}`
        },
        body: JSON.stringify(formData),
      })
          .then(response => {
            console.log(response.status)
            if (response.status === 201) {
              return response.json();
            }
            else if (response.status === 401){
              alert("Вы не авторизированы");
              throw new Error("Unauthorized");

            }
            else {
              throw new Error("Unexpected error");
            }

          })
          .then(data => {

            console.log("принятные данные: ", data);
            this.$emit('formSubmitted', data);
          })
          .catch(error => {
            console.error('Ошибка при отправке формы:', error);
          });
    }
  },
  watch: {
    r(newVal) {
      this.$emit("r-updated", newVal);
    }
  }
}
</script>

<style scoped>
input[type="text"] {
  background-color: #053C46;
  color: #fff;
  border: 1px solid #074754;
  padding: 10px;
  margin: 10px 0;
  border-radius: 4px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
}

input[type="text"]:focus {
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.4), 0 0 6px #65dfc9;
}

.selected {
  background-color: #074754;
  color: #65dfc9;
}

.sendForm {
  display: flex;
  flex-direction: column;
  margin: 20px;
}

#choose-x, #choose-y, #choose-r {
  margin-bottom: 10px;
}

button {
  background-color: #65dfc9;
  color: #074754;
  border: 2px solid #074754;
  border-radius: 4px;
  padding: 10px 20px;
  margin: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
}

button:hover {
  background-color: #074754;
  color: #65dfc9;
  transform: translateY(-2px);
}

@media (max-width: 875px) {
  .sendForm {
    width: 100%;
  }
}
</style>