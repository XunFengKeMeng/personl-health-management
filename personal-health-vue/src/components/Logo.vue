<template>
  <span class="logo">
    <el-image
      style="width:25px; height: 25px"
      src="/logo.png"
      fit="fill"
    ></el-image>
    <div
      class="sysName"
      v-if="!flag"
    >
      <span>{{ sysName }}</span>
    </div>
  </span>
</template>
<script>
export default {
  name: "AppLogo",
  data () {
    return {

    }
  },
  props: {
    flag: {
      type: Boolean,
      required: false
    },
    sysName: {
      type: String,
      required: false
    },
  },
  created () {

  },
  methods: {

  }
};
</script>
<style scoped lang="scss">
.logo {
  color: rgb(8, 24, 16) !important;
  font-weight: bold;
  font-size: 20px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  user-select: none;
  min-width: 250px;

  span {
    margin-left: 8px;
    line-height: 25px;
  }
}
</style>