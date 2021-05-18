<template>

    <div class="commands">
        
        <h1>Commands</h1>
        
        <Command v-for="command of commands" :command="command"></Command>
        
    </div>
    
</template>

<script>
import axios from "axios";
import Command from "../components/Command";

export default {
    name: "Commands",
    components: {Command},
    data() {
        return {
            commands: []
        }
    },
    async mounted() {
        
        try {
            let res = await axios.get("/discordInfo");
            this.commands = res.data.commands;
        } catch (e) {
            console.error(e);
        }
        
    }
}
</script>

<style scoped>

</style>