<template>
  <div class="table-container">
    <button id="clear-button" @click="clearTable">
      Clear table
    </button>
    <table class="table" id="result-table">
      <thead>
      <tr>
        <th>X</th>
        <th>Y</th>
        <th>R</th>
        <th>Result</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="response in history" :key="response.id">
        <td>{{ response.x }}</td>
        <td>{{ response.y }}</td>
        <td>{{ response.r }}</td>
        <td>{{ response.result }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: "ResultTable",
  props: {
    history: Array
  },
  methods: {
    clearTable(){
      const jwtToken = sessionStorage.getItem('jwtToken');
      fetch('http://localhost:8080/api/clearTable', {
        method: 'DELETE',
        headers:{
          "Authorization": `Bearer ${jwtToken}`
        }
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            this.$emit('clearTable');
          })
          .catch(error => {
            console.error('Ошибка при отправке формы:', error);
          });
    }
  }
}
</script>

<style scoped>
.table-container {
  background-color: #053C46;
  color: #fff;
  max-height: 400px;
  overflow-y: auto;
  border: 2px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin: 20px 0;
}

#clear-button {
  padding: 8px 16px;
  margin: 3px;
  background-color: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

#clear-button:hover {
  background-color: #ff1a1a;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th, .table td {
  border-bottom: 1px solid #074754;
}

.table th {
  text-align: left;
  background-color: #4182bd;
  color: white;
}

.table tr:nth-child(even) {
  background-color: #065A64;
}

.table tr:hover {
  background-color: #5454c0;
}
@media (max-width: 875px) {
  .table-container {
    width: 100%;
  }
}
</style>