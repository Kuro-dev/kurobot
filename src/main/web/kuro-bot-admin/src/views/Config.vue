<template>

    <div class="configuration">
        
        <h1>Configuration</h1>
        
        <Table>
            <tr>
                <th>Config parameter</th>
                <th>Config value</th>
            </tr>
            <tr v-for="[key, value] of Object.entries(config)">
                <td>{{ key }}</td>
                <td>{{ value }}</td>
            </tr>
        </Table>
        
    </div>
    
</template>

<script>
import Table from "../components/Table";
import axios from "axios";
export default {
    name: "Config",
    components: {Table},
    data() {
        return {
            config: {}
        }
    },
    async mounted() {
        
        try {
            let res = await axios.get("/config");
            this.config = res.data;
        } catch (e) {
            console.error(e);
        }
        
    }
    
}
</script>

<style scoped>
table td:first-child {
    font-weight: bold;
}
</style>