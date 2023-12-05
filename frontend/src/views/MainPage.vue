<template>
  <div class="main">
    <HeaderComponent/>
    <div class="main-container">

      <GraphCanvas ref="graphCanvas"  :tableData="tableData" :responseData="graphData" :currentR="r"
                   @clickOnCanvas="handleClick"/>
      <InputFields @formSubmitted="handleServerResponse" @r-updated="handleRUpdate"/>
      <ResultTable :history="tableData" @clearTable="clearTable"/>
    </div>
  </div>
</template>

<script>
import HeaderComponent from "@/components/HeaderComponent.vue";
import GraphCanvas from "@/components/GraphCanvas.vue";
import InputFields from "@/components/InputFields.vue";
import ResultTable from "@/components/ResultTable.vue";


export default {
  name: "MainPage",
  mounted() {
    this.fetchUserData();
  },
  data(){
    return {
      tableData: [],
      graphData: null,
      r: null,
    }
  },
  components: {
    ResultTable,
    InputFields,
    GraphCanvas,
    HeaderComponent
  },
  methods: {

    fetchUserData() {
      const jwtToken = sessionStorage.getItem('jwtToken');
      console.log(jwtToken);
      if (!jwtToken) {
        console.log("logOut");
        return;

      }

      fetch("http://localhost:8080/api/shots", {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${jwtToken}`,
          'Content-Type': 'application/json'
        },
      })
          .then(response => {
            console.log(response.status);
            if (!response.ok) {
              throw new Error('Failed to fetch user data');
            }
            return response.json();
          })
          .then(data => {
            this.tableData = data;
          })
          .catch(error => console.error('Error fetching user data:', error));
    },


    handleClick(data){
      this.tableData.push(data);
    },

    handleServerResponse(data) {
      this.graphData = data;
      this.tableData.push(data);
    },
    handleRUpdate(newR){
      this.r = newR;
    },
    clearTable(){
      this.tableData = [];
      this.$refs.graphCanvas.clearCanvas();
    }
  }
}
</script>

<style>
*{
  padding: 0;
  margin: 0;
}
.main-container {
  background: linear-gradient(to right top, #65dfc9, #074754);
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  padding: 20px;
  margin: 20px 0;
}
.main {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(to right top, #65dfc9, #074754);
}

button {
  padding: 8px 16px;
  margin-left: 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #45a049;
}
@media (min-width: 1212px) {
  .main {
    padding: 0 15%;
    /*max-width: 1200px;
    margin: auto; /* Автоматические отступы для центрирования */
  }

  .main-container {
    max-width: 12000px;
    margin: auto;
    padding: 40px;
  }

  button {
    padding: 12px 24px;
    font-size: 1em;
  }

}

/* Планшетный режим */
@media (min-width: 876px) and (max-width: 1211px) {
  .main {
    padding: 0 10%;
  }
}

/* Мобильный режим */
@media (max-width: 875px) {
  .main {
    padding: 0 5%;
  }

}
</style>
