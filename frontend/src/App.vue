<template>
  <div class="container flex-row d-flex">
    <div class="col-6 py-3">
      <div class="row">
        <b-form-input
            id="input-1"
            type="email"
            placeholder="Filter"
            v-model="filterQuery"
        ></b-form-input>


        <Tweet v-for="(item, index) in tweets" :key="index" :data="item"/>
      </div>
    </div>

    <div class="col-5 mt-5">
      <div class="row">
        <h4>Number of tweets: {{ tweets.length }}</h4>
        <canvas id="pie-chart"></canvas>
      </div>
    </div>

  </div>
</template>

<script>
import Chart from 'chart.js';
import Tweet from "@/components/Tweet";


export default {
  name: 'App',
  components: {Tweet},
  data() {
    return {
      filterQuery: "",
      stats: [],
      tweets: [],
      chart: null
    }
  },
  methods: {
    getTweets() {
      this.$http.get('/tweets', {
        params: {filter: this.filterQuery}
      }).then((res) => {
        const data = res.data;
        this.stats = data.stats;

        this.tweets = data.tweets;

        if (this.chart === null) {
          const ctx = document.getElementById('pie-chart');

          this.chart = new Chart(ctx, {
            type: 'pie',
            data: {
              datasets: [{
                data: data.stats,
                backgroundColor: [
                  'rgb(147,0,0)',
                  'rgb(255,0,57)',
                  'rgb(160,160,160)',
                  'rgb(0,255,0)',
                  'rgb(0,189,0)',
                ]
              }],
              labels: [
                'Very Negative',
                'Negative',
                'Neutral',
                'Positive',
                'Very Positive',
              ]
            },
            options: Chart.defaults.pie
          });
        } else {
          this.chart.data.datasets[0].data = data.stats;
          this.chart.update();
        }

      }).catch((err) => {
        console.log(err)
      })
    }
  },
  created() {
  },
  mounted() {
    this.getTweets()
    const ctx = document.getElementById('pie-chart');

    var data = {
      datasets: [{
        data: this.stats,
        backgroundColor: [
          'rgb(147,0,0)',
          'rgb(255,0,57)',
          'rgb(160,160,160)',
          'rgb(0,255,0)',
          'rgb(0,189,0)',
        ]
      }],
      labels: [
        'Very Negative',
        'Negative',
        'Neutral',
        'Positive',
        'Very Positive',
      ]
    }

    let chart = new Chart(ctx, {
      type: 'pie',
      data: data,
      options: Chart.defaults.pie
    });

    chart.update();
    console.log(data)
  }
}
</script>

<style>

</style>
