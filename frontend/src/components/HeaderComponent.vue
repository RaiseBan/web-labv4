<template>
  <div class="header">
    <header id="header">
      <span>Shornikov Sergei P3211 Variant ...</span>
    </header>
    <button @click="logOut">Log out</button>

  </div>
</template>

<script>
export default {
  name: 'HeaderComponent',
  methods: {
    logOut() {
      sessionStorage.clear();
      this.$router.go(-1);
    }
  }
}

</script>

<style scoped>
.header {
  display: flex;
  color: #9eabf1;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 10px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background: linear-gradient(90deg, rgba(2,0,36,1) 0%, rgba(45,45,130,1) 15%, rgba(32,33,69,1) 24%, rgba(43,112,175,1) 36%, rgba(17,62,205,1) 48%, rgba(66,134,121,1) 55%, rgba(12,106,219,1) 64%, rgba(166,11,221,1) 75%, rgba(149,208,247,1) 87%, rgba(0,212,255,1) 100%);
  border-radius: 5px;
}

#header span {
  font-weight: bold;
}

button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

}

button:hover {
  background-color: #45a049;
}

/* Десктопный режим */
@media (min-width: 1212px) {
  .header {
    padding: 15px 30px;
  }

  #header span {
    font-size: 1.2em;
  }

  button {
    padding: 10px 20px;
  }
}

/* Планшетный режим */
@media (min-width: 876px) and (max-width: 1211px) {
  .header {
    padding: 12px 25px;
  }

  #header span {
    font-size: 1.1em;
  }

  button {
    padding: 9px 18px;
  }
}

/* Мобильный режим */
@media (max-width: 875px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px;
  }

  #header span {
    margin-bottom: 10px;
    font-size: 1em;
  }

  button {
    width: 30%;
    margin-bottom: 10px;
  }
}
</style>