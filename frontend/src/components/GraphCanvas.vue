<template>
  <div class="div-canvas">
    <canvas @click="clickOnCanvas" id="canvas" width="600" height="500">

    </canvas>

  </div>
</template>

<script>
import Toastify from 'toastify-js'
import 'toastify-js/src/toastify.css'
export default {
  name: "GraphCanvas",
  data() {
    return {
      userInputX: 0,
      userInputY: 0,
      userInputR: 2,
      lastResult: "",
      canvas: null,
      ctx: null,
      widthCenter: 0,
      heightCenter: 0
    }
  },
  mounted() {
    this.canvas = document.getElementById("canvas");
    this.ctx = this.canvas.getContext("2d");
    this.widthCenter = this.canvas.width / 2;
    this.heightCenter = this.canvas.height / 2;
    this.draw();

  },

  props: {
    responseData: {
      type: [Object],
      default: () => {
      }
    },
    tableData: Array,
    currentR: null,

  },
  watch: {
    currentR(newR) {
      this.userInputR = newR;
      this.redrawAllPoints();
    },
    tableData() {
      this.redrawAllPoints();
    },
    responseData(data){
      this.userInputX = data.x;
      this.userInputY = data.y;
      this.userInputR = data.r;
      this.drawPoint(data.result);
    }

  },
  methods: {
    clearCanvas() {
      this.draw();
      this.draw();
    },
    redrawAllPoints() {
      this.ctx.clearRect(0, 0, 1000, 1000);
      this.draw();
      for (let i = 0; i < this.tableData.length; i++) {
        let r = this.userInputR === null?this.currentR: this.userInputR;
        this.drawPointWithCords(this.hit(this.tableData[i].x, this.tableData[i].y, Math.abs(r)), this.tableData[i].x, this.tableData[i].y, Math.abs(r));
      }
    },
    drawPointWithCords(flag, x, y, r) {


      if (flag.trim() === "In") {
        this.ctx.fillStyle = "green";
      } else {
        this.ctx.fillStyle = "red";
      }

      this.ctx.beginPath();
      this.ctx.arc(this.widthCenter + 100 * parseFloat(x) / parseFloat(r), this.heightCenter - 100 * parseFloat(y) / parseFloat(r), 3, 0, 2 * Math.PI);
      this.ctx.closePath();
      this.ctx.fill();
    },
    hit(x, y, r) {
      if (x > 0 && y < 0){
        return this.hitTriangle(x, y, r);
      }
      if (x < 0 && y > 0) {
        return this.hitCircle(x, y, r);

      }
      if (x < 0 && y < 0){
        return this.hitSquare(x, y, r);
      }
      if ((x === 0 && y <= r && y >= -r) || (y === 0 && x <= r && x >= -r)){
        return "In";
      }else{
        return "Out";
      }
    },
    hitCircle(x, y, r) {
      if (x*x + y*y <= r*r/4){
        return "In";
      }
      return "Out";
    },

    hitSquare(x, y, r) {
      if (x >= -r && y >= -r){
        return "In";
      }
      return "Out";
    },

    hitTriangle(x, y, r) {
      if (y >= x - r){
        return "In";
      }
      return "Out";
    },


    sendToServer(x, y, r) {
      const formData = {
        x: x,
        y: y,
        r: r
      };
      const jwtToken = sessionStorage.getItem('jwtToken');
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
            if (response.status !== 201) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {

            this.$emit('clickOnCanvas', data);
            this.drawPoint(data.result);
          })
          .catch(error => {
            console.error('Ошибка при отправке формы:', error);
          });
    },


    clickOnCanvas() {
      if (this.userInputR < 1) {
        Toastify({
          text: "Значение R невлидно! \n R > 0!",
          duration: 3000
        }).showToast();
        return;
      }
      let rVal = this.userInputR;
      const rect = this.canvas.getBoundingClientRect();
      const clickedX = event.clientX - rect.left;
      const clickedY = event.clientY - rect.top;

      const graphX = (clickedX - this.widthCenter) * rVal / 100;
      const graphY = (this.heightCenter - clickedY) * rVal / 100;

      this.userInputX = graphX;
      this.userInputY = graphY;
      this.sendToServer(this.userInputX, this.userInputY, rVal);
    },


    drawPoint(flag) {
      if (Math.abs(this.userInputY) > this.userInputR || Math.abs(this.userInputX) > this.userInputR) {
        Toastify({
          text: "Выход за граница графика!",
          duration: 3000,
        }).showToast();
      } else {

        if (flag.trim() === "In") {
          this.ctx.fillStyle = "green";
        } else {
          this.ctx.fillStyle = "red";
        }

        this.ctx.beginPath();
        this.ctx.arc(this.widthCenter + 100 * this.userInputX / this.userInputR, this.heightCenter - 100 * parseFloat(this.userInputY) / this.userInputR, 3, 0, 2 * Math.PI);
        this.ctx.closePath();
        this.ctx.fill();

      }

    },
    draw() {

      this.ctx.clearRect(0, 0, 1000, 1000);

      this.ctx.beginPath();
      this.ctx.fillStyle = "rgba(249,192,116,256)";
      this.ctx.fillRect(this.widthCenter, this.heightCenter, -100, 100);

      this.ctx.moveTo(this.widthCenter, this.heightCenter);
      this.ctx.arc(this.widthCenter, this.heightCenter, 50, Math.PI, -Math.PI / 2);
      this.ctx.fill();
// треугольник
      this.ctx.beginPath();
      this.ctx.moveTo(this.widthCenter, this.heightCenter);
      this.ctx.lineTo(this.widthCenter, this.heightCenter + 100);
      this.ctx.lineTo(this.widthCenter + 100, this.heightCenter);
      this.ctx.fill();


      this.ctx.fillStyle = "black"
      this.ctx.beginPath();
      this.ctx.moveTo(this.widthCenter, this.heightCenter);
// линия x
      this.ctx.lineTo(this.widthCenter + 125, this.heightCenter); // добавляем линию
      this.ctx.moveTo(this.widthCenter, this.heightCenter); // начальная точка пути
      this.ctx.lineTo(this.widthCenter - 125, this.heightCenter); // добавляем линию

// //линия y
      this.ctx.moveTo(this.widthCenter, this.heightCenter); // начальная точка пути
      this.ctx.lineTo(this.widthCenter, this.heightCenter + 125); // добавляем линию
      this.ctx.moveTo(this.widthCenter, this.heightCenter); // начальная точка пути
      this.ctx.lineTo(this.widthCenter, this.heightCenter - 125); // добавляем линию


//стрелочка для y
      this.ctx.moveTo(this.widthCenter, this.heightCenter - 125);
      this.ctx.lineTo(this.widthCenter - 5, this.heightCenter - 120);
      this.ctx.moveTo(this.widthCenter, this.heightCenter - 125);
      this.ctx.lineTo(this.widthCenter + 5, this.heightCenter - 120);
      this.ctx.fillText('Y', this.widthCenter, this.heightCenter - 125);

//стрелочка для X
      this.ctx.moveTo(this.widthCenter + 125, this.heightCenter);
      this.ctx.lineTo(this.widthCenter + 120, this.heightCenter - 5);
      this.ctx.moveTo(this.widthCenter + 125, this.heightCenter);
      this.ctx.lineTo(this.widthCenter + 120, this.heightCenter + 5);
// линия x, засечка R/2
      this.ctx.moveTo(this.widthCenter + 50, this.heightCenter - 5);
      this.ctx.lineTo(this.widthCenter + 50, this.heightCenter + 5);
      this.ctx.fillText(-1 * this.userInputR / 2, this.widthCenter + 50, this.heightCenter - 8);
// линия x, засечка R
      this.ctx.moveTo(this.widthCenter + 100, this.heightCenter - 5);
      this.ctx.lineTo(this.widthCenter + 100, this.heightCenter + 5);
      this.ctx.fillText(-1 * this.userInputR, this.widthCenter + 100, this.heightCenter - 8);
// перемещаемся обратно в центр
      this.ctx.moveTo(this.widthCenter, this.heightCenter);
// линия x, засечка -R/2
      this.ctx.moveTo(this.widthCenter - 50, this.heightCenter - 5);
      this.ctx.lineTo(this.widthCenter - 50, this.heightCenter + 5);
      this.ctx.fillText(-1 * this.userInputR / 2, this.widthCenter - 50, this.heightCenter - 8);
// линия x, засечка -R
      this.ctx.moveTo(this.widthCenter - 100, this.heightCenter - 5);
      this.ctx.lineTo(this.widthCenter - 100, this.heightCenter + 5);
      this.ctx.fillText(-1 * this.userInputR, this.widthCenter - 100, this.heightCenter - 8);

//линия y, засечка R/2
      this.ctx.moveTo(this.widthCenter, this.heightCenter);
      this.ctx.moveTo(this.widthCenter - 5, this.heightCenter - 50);
      this.ctx.lineTo(this.widthCenter + 5, this.heightCenter - 50);
      this.ctx.fillText(this.userInputR / 2, this.widthCenter + 10, this.heightCenter - 45);
//линия y, засечка R
      this.ctx.moveTo(this.widthCenter - 5, this.heightCenter - 100);
      this.ctx.lineTo(this.widthCenter + 5, this.heightCenter - 100);
      this.ctx.fillText(this.userInputR, this.widthCenter + 10, this.heightCenter - 95);
//
      this.ctx.moveTo(this.widthCenter, this.heightCenter);
//линия y, засечка -R/2
      this.ctx.moveTo(this.widthCenter - 5, this.heightCenter + 50);
      this.ctx.lineTo(this.widthCenter + 5, this.heightCenter + 50);
      this.ctx.fillText(-1 * this.userInputR / 2, this.widthCenter + 10, this.heightCenter + 55);
//линия y, засечка -R
      this.ctx.moveTo(this.widthCenter - 5, this.heightCenter + 100);
      this.ctx.lineTo(this.widthCenter + 5, this.heightCenter + 100);
      this.ctx.fillText(-1 * this.userInputR, this.widthCenter + 10, this.heightCenter + 105);


      this.ctx.strokeStyle = "black"; // устанавливаем цвет контура
      this.ctx.stroke(); // рисуем контур пути
      this.ctx.closePath();

    }
  }
}
</script>

<style scoped>
.div-canvas {
  border: 2px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
canvas {
  height: auto;
  display: block;
  border-radius: 8px;
  background-color: transparent;
}
@media (max-width: 875px) {
  .div-canvas {
    width: 100%;
    height: auto;
  }
  canvas {
    max-width: 100%;
    height: auto;
  }
}
</style>