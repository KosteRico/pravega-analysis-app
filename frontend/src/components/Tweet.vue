<template>
  <div class="card my-3" :style="{width: '22rem', background: this.color}">
    <div class="card-body">
      <p class="card-text">{{ data.text }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Tweet",
  props: ['data'],
  computed: {
    color() {
      var backgroundColors = [
        'rgba(147,0,0, .4)',
        'rgba(255,0,57, .4)',
        'rgba(160,160,160, .4)',
        'rgba(0,255,0, .4)',
        'rgba(0,189,0, .4)',
      ]

      return backgroundColors[this.data.type]
    }
  }
}
</script>

<style scoped>

</style>