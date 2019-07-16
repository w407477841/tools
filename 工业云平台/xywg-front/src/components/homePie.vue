<template>
  <div id="homePie" ref="homePie"></div>
</template>

<script>
export default {
  props: {
    dataSourse: {
      type: Array,
      default: []
    },
    legendData: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      mychart: null
    };
  },
  mounted() {
    this.myChart = this.echarts.init(this.$refs.homePie);
    let option = {
      color: ["#007cff", "#28ae64", "#ffba31", "#ff5855", "#c772ff"],
      silent: true,
      tooltip: {
        trigger: "item",
        // formatter: "{a} <br/>{b}: {c} ({d}%)"
        formatter:
          "<div style='border-bottom:1px solid #e7e9ed;padding:8px;'>{b}</div><div style='padding:8px;'><span style='display:inline-block;width:6px;height:6px;background-color:#007cff;border-radius:100%;margin-right:5px;'></span>{c} {d}%</div> ",
        backgroundColor: "rgba(255,255,255,1)",
        padding: 0,
        boxShadow: "10px 10px 5px #888888;",
        textStyle: {
          color: "#130a33"
        },
        extraCssText:
          "box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.2);border-radius:0;"
      },
      legend: {
        selectedMode: false,
        orient: "vertical",
        right: "7%",
        top: "30%",
        icon: "circle",
        data: this.legendData
      },
      series: [
        {
          name: "访问来源",
          type: "pie",
          radius: ["50%", "65%"],
          center: ["30%", "50%"],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: false,
              position: "center"
            },
            emphasis: {
              show: true,
              textStyle: {
                fontSize: "30",
                fontWeight: "bold"
              }
            }
          },
          labelLine: {
            normal: {
              show: false
            }
          },
          data: this.dataSourse
        }
      ]
    };
    this.myChart.setOption(option);
    addEventOnResize(() => {
      this.myChart.resize();
    });
  },
  watch: {
    dataSource() {
      let option = {
        color: ["#007cff", "#28ae64", "#ffba31", "#ff5855", "#c772ff"],
        silent: true,
        tooltip: {
          trigger: "item",
          // formatter: "{a} <br/>{b}: {c} ({d}%)"
          formatter:
            "<div style='border-bottom:1px solid #e7e9ed;padding:8px;'>{b}</div><div style='padding:8px;'><span style='display:inline-block;width:6px;height:6px;background-color:#007cff;border-radius:100%;margin-right:5px;'></span>{c} {d}%</div> ",
          backgroundColor: "rgba(255,255,255,1)",
          padding: 0,
          boxShadow: "10px 10px 5px #888888;",
          textStyle: {
            color: "#130a33"
          },
          extraCssText:
            "box-shadow: 0px 4px 8px 0px rgba(0, 0, 0, 0.2);border-radius:0;"
        },
        legend: {
          selectedMode: false,
          orient: "vertical",
          right: "7%",
          top: "30%",
          icon: "circle",
          data: this.legendData
        },
        series: [
          {
            name: "访问来源",
            type: "pie",
            radius: ["50%", "65%"],
            center: ["30%", "50%"],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: "center"
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold"
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: this.dataSourse
          }
        ]
      };
      this.myChart.setOption(option);
      addEventOnResize(() => {
        this.myChart.resize();
      });
    }
  }
};
</script>

<style lang="less" scoped>
</style>

