<template>
    <span class="label" :class="[color]">
        <slot/>
    </span>
</template>

<script>
export default {
    name: "Label",
    props: {
        color: String
    }
}
</script>

<style scoped>
.label {
    padding: 4px 8px;
    color: white;
    font-size: 14px;
    font-weight: 600;
    display: inline-flex;
    border-radius: var(--border-radius);
    background-color: var(--color-primary-dark);
    margin-right: 10px;
}

.label:last-child {
    margin: 0;
}

.label.green {
    background-color: var(--color-green);
}

.label.red {
    background-color: var(--color-red);
}

.label.blue {
    background-color: var(--color-blue);
}

.label.orange {
    background-color: var(--color-orange);
}
</style>