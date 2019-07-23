// export default (name) => () => import(`@/components${name}.vue`)
export default (name) => resolve => require([`@/components${name}.vue`], resolve)
// resolve => require(['../components/page/BaseTable.vue'], resolve)